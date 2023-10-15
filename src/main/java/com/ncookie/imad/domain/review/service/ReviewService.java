package com.ncookie.imad.domain.review.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.review.dto.request.AddReviewRequest;
import com.ncookie.imad.domain.review.dto.request.ModifyReviewRequest;
import com.ncookie.imad.domain.review.dto.response.AddReviewResponse;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.dto.response.ReviewListResponse;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review.repository.ReviewRepository;
import com.ncookie.imad.domain.like.entity.ReviewLike;
import com.ncookie.imad.domain.like.service.ReviewLikeService;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserAccountService userAccountService;
    private final ContentsService contentsService;

    private final ReviewLikeService reviewLikeService;

    private final int PAGE_SIZE = 10;

    public ReviewDetailsResponse getReview(String accessToken, Long reviewId) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            ReviewLike reviewLike = reviewLikeService.findByUserAccountAndE(user, review);
            int likeStatus = reviewLike == null ? 0 : reviewLike.getLikeStatus();

            ReviewDetailsResponse reviewDetailsResponse = ReviewDetailsResponse.toDTO(review);
            reviewDetailsResponse.setUserId(user.getId());
            reviewDetailsResponse.setLikeStatus(likeStatus);

            return reviewDetailsResponse;
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    public ReviewListResponse getReviewList(String accessToken, Long contentsId, int pageNumber, String sortString, int order) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(contentsId);

        // sort가 null이거나, sort 설정 중 에러가 발생했을 때의 예외처리도 해주어야 함
        Sort sort;
        PageRequest pageable;
        try {
            if (order == 0) {
                // 오름차순 (ascending)
                sort = Sort.by(sortString).ascending();
                pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
            } else if (order == 1) {
                // 내림차순 (descending)
                sort = Sort.by(sortString).descending();
                pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
            } else {
                pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
            }

            Page<Review> reviewPage = reviewRepository.findAllByContents(contents, pageable);
            return ReviewListResponse.toDTO(
                    reviewPage,
                    convertReviewListToReviewDetailsResponse(user, reviewPage.getContent().stream().toList())
            );
        } catch (PropertyReferenceException e) {
            // sort string에 잘못된 값이 들어왔을 때 에러 발생
            throw new BadRequestException(ResponseCode.REVIEW_WRONG_SORT_STRING);
        }
    }

    public ReviewListResponse getReviewListByUser(UserAccount user, int pageNumber) {
        Sort sort = Sort.by("createdDate").descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
        Page<Review> reviewPage = reviewRepository.findAllByUserAccount(user, pageable);

        return ReviewListResponse.toDTO(
                reviewPage,
                convertReviewListToReviewDetailsResponse(user, reviewPage.getContent().stream().toList())
        );
    }

    public ReviewListResponse getLikedReviewListByUser(UserAccount user, int pageNumber) {
        Sort sort = Sort.by("createdDate").descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
        Page<ReviewLike> reviewLikePage = reviewLikeService.getLikedListByUser(user, pageable);

        List<Review> reviewList = new ArrayList<>();
        for (ReviewLike reviewLike : reviewLikePage.getContent().stream().toList()) {
            reviewList.add(reviewLike.getReview());
        }

        return ReviewListResponse.toDTO(
                reviewLikePage,
                convertReviewListToReviewDetailsResponse(user, reviewList)
        );
    }

    private List<ReviewDetailsResponse> convertReviewListToReviewDetailsResponse(UserAccount user, List<Review> reviewList) {
        // Review 클래스를 ReviewDetailsResponse 데이터 형식에 맞게 매핑
        List<ReviewDetailsResponse> reviewDetailsResponseList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewLike reviewLike = reviewLikeService.findByUserAccountAndE(user, review);
            int likeStatus = reviewLike == null ? 0 : reviewLike.getLikeStatus();

            // DTO 클래스 변환 및 like status 설정
            ReviewDetailsResponse reviewDetailsResponse = ReviewDetailsResponse.toDTO(review);
            reviewDetailsResponse.setLikeStatus(likeStatus);
            reviewDetailsResponseList.add(reviewDetailsResponse);
        }

        return reviewDetailsResponseList;
    }
    

    public AddReviewResponse addReview(String accessToken, AddReviewRequest addReviewRequest) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(addReviewRequest.getContentsId());

        // 유저는 작품에 대해 한 가지 리뷰만 작성할 수 있음
        if (reviewRepository.existsReviewByUserAccountAndContents(user, contents)) {
            throw new BadRequestException(ResponseCode.REVIEW_ALREADY_REGISTERED);
        }

        if (contents == null) {
            throw new BadRequestException(ResponseCode.CONTENTS_ID_NOT_FOUND);
        }

        Review review = reviewRepository.save(
                Review.builder()
                .userAccount(user)
                .contents(contents)

                .title(addReviewRequest.getTitle())
                .content(addReviewRequest.getContent())

                .score(addReviewRequest.getScore())
                .isSpoiler(addReviewRequest.isSpoiler())

                .build());

        calculateAndSaveAverageScore(review);

        return AddReviewResponse.builder()
                .reviewId(review.getReviewId())
                .build();
    }

    public Long modifyReview(String accessToken, Long reviewId, ModifyReviewRequest reviewRequest) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            // 해당 리뷰를 작성한 유저만 수정할 수 있음
            if (Objects.equals(review.getUserAccount().getId(), user.getId())) {
                review.setTitle(reviewRequest.getTitle());
                review.setContent(reviewRequest.getContent());
                review.setScore(reviewRequest.getScore());
                review.setSpoiler(reviewRequest.isSpoiler());

                calculateAndSaveAverageScore(review);

                return reviewRepository.save(review).getReviewId();
            } else {
                throw new BadRequestException(ResponseCode.REVIEW_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    public void deleteReview(String accessToken, Long reviewId) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            // 해당 리뷰를 작성한 유저만 삭제할 수 있음
            if (Objects.equals(review.getUserAccount().getId(), user.getId())) {
                reviewRepository.delete(review);
                calculateAndSaveAverageScore(review);
            } else {
                throw new BadRequestException(ResponseCode.REVIEW_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    public void saveLikeStatus(String accessToken, Long reviewId, int likeStatus) {
        if (likeStatus != -1 && likeStatus != 0 && likeStatus != 1) {
            throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
        }

        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();

            ReviewLike reviewLike = reviewLikeService.findByUserAccountAndE(user, review);

            // reviewLike 신규등록
            if (reviewLike == null) {
                reviewLikeService.saveLikeStatus(ReviewLike.builder()
                        .userAccount(user)
                        .review(review)
                        .likeStatus(likeStatus)
                        .build());
            } else {
                // like_status가 1이면 좋아요, -1이면 싫어요, 0이면 둘 중 하나를 취소한 상태이므로 테이블에서 데이터 삭제
                if (likeStatus == 0) {
                    reviewLikeService.deleteLikeStatus(reviewLike);
                } else {
                    reviewLike.setLikeStatus(likeStatus);
                    ReviewLike savedReviewLikeStatus = reviewLikeService.saveLikeStatus(reviewLike);

                    // reviewLike entity 저장/수정 실패
                    if (savedReviewLikeStatus == null) {
                        throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
                    }
                }
            }

            // like, dislike count 갱신
            reviewRepository.updateLikeCount(review.getReviewId(), reviewLikeService.getLikeCount(review));
            reviewRepository.updateDislikeCount(review.getReviewId(), reviewLikeService.getDislikeCount(review));
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }


    // 작품 평점 갱신
    private void calculateAndSaveAverageScore(Review review) {
        Contents contents = review.getContents();
        contents.setReviewCnt(reviewRepository.countReviewForContents(contents));
        contents.setImadScore(reviewRepository.calculateAverageScoreForContents(contents));

        contentsService.saveContentsScoreAndReviewCount(contents);
    }
}
