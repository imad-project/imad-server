package com.ncookie.imad.domain.review.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.ranking.service.ContentsRankingScoreUpdateService;
import com.ncookie.imad.domain.ranking.service.TodayPopularReviewService;
import com.ncookie.imad.domain.ranking.service.TodayPopularScoreService;
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
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ncookie.imad.domain.ranking.service.ContentsRankingScoreUpdateService.REVIEW_RANKING_SCORE;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRetrievalService userRetrievalService;
    private final ContentsService contentsService;

    private final ReviewLikeService reviewLikeService;

    private final ContentsRankingScoreUpdateService contentsRankingScoreUpdateService;
    private final TodayPopularReviewService todayPopularReviewService;

    public ReviewDetailsResponse getReview(String accessToken, Long reviewId) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            // 본인 작성 여부
            boolean isAuthor;
            if (user == null) {
                isAuthor = false;
            } else {
                isAuthor = review.getUserAccount().getId().equals(user.getId());
            }

            ReviewLike reviewLike = reviewLikeService.findByUserAccountAndE(user, review);
            int likeStatus = reviewLike == null ? 0 : reviewLike.getLikeStatus();

            ReviewDetailsResponse reviewDetailsResponse = ReviewDetailsResponse.toDTO(review);
            reviewDetailsResponse.setAuthor(isAuthor);
            reviewDetailsResponse.setLikeStatus(likeStatus);

            return reviewDetailsResponse;
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    public ReviewListResponse getReviewList(String accessToken, Long contentsId, int pageNumber, String sortString, int order) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(contentsId);

        // sort가 null이거나, sort 설정 중 에러가 발생했을 때의 예외처리도 해주어야 함
        PageRequest pageable = Utils.getPageRequest(pageNumber, sortString, order);

        Page<Review> reviewPage = reviewRepository.findAllByContents(contents, pageable);
        return ReviewListResponse.toDTO(
                reviewPage,
                convertReviewListToReviewDetailsResponse(user, reviewPage.getContent().stream().toList())
        );
    }

    public ReviewListResponse getReviewListByUser(UserAccount user, int pageNumber) {
        Page<Review> reviewPage = reviewRepository.findAllByUserAccount(user, Utils.getDefaultPageable(pageNumber));

        return ReviewListResponse.toDTO(
                reviewPage,
                convertReviewListToReviewDetailsResponse(
                        user,
                        reviewPage.getContent().stream().toList())
        );
    }

    public ReviewListResponse getLikedReviewListByUser(UserAccount user, int pageNumber, int likeStatus) {
        Page<ReviewLike> reviewLikePage = reviewLikeService.getLikedListByUser(
                user,
                Utils.getDefaultPageable(pageNumber),
                likeStatus);

        List<Review> reviewList = new ArrayList<>();
        for (ReviewLike reviewLike : reviewLikePage.getContent().stream().toList()) {
            reviewList.add(reviewLike.getReview());
        }

        return ReviewListResponse.toDTO(
                reviewLikePage,
                convertReviewListToReviewDetailsResponse(user, reviewList)
        );
    }

    public ReviewDetailsResponse getMostLikeReview() {
        List<Review> mostLikeReviewList = reviewRepository.findMostLikeReview();

        if (mostLikeReviewList.isEmpty()) {
            return null;
        }

        if (mostLikeReviewList.size() > 1) {
            int randomNum = Utils.getRandomNum(mostLikeReviewList.size());
            return ReviewDetailsResponse.toDTO(mostLikeReviewList.get(randomNum));
        }
        return ReviewDetailsResponse.toDTO(mostLikeReviewList.get(0));
    }

    public int getWrittenReviewCount(UserAccount user) {
        return reviewRepository.countWrittenReviewByUser(user);
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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
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

        contentsRankingScoreUpdateService.addRankingScore(contents, REVIEW_RANKING_SCORE);
        log.info("[리뷰 작성] 랭킹 점수 반영 완료");

        return AddReviewResponse.builder()
                .reviewId(review.getReviewId())
                .build();
    }

    public Long modifyReview(String accessToken, Long reviewId, ModifyReviewRequest reviewRequest) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            // 해당 리뷰를 작성한 유저만 삭제할 수 있음
            if (Objects.equals(review.getUserAccount().getId(), user.getId())) {
                reviewRepository.delete(review);
                calculateAndSaveAverageScore(review);
                
                contentsRankingScoreUpdateService.subtractRankingScore(review.getContents(), REVIEW_RANKING_SCORE);
                log.info("[리뷰 삭제] 랭킹 점수 반영 완료");
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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

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
                // 기존의 좋아요를 좋아요 취소 또는 싫어요로 변경했을 때 인기 점수 차감
                int previousLikeStatus = reviewLike.getLikeStatus();
                if (previousLikeStatus == 1 && (likeStatus == 0 || likeStatus == -1)) {
                    todayPopularReviewService.subtractPopularScore(review, TodayPopularScoreService.POPULAR_LIKE_SCORE);
                }

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

            // 리뷰 인기 점수 추가
            if (likeStatus == 1) {
                todayPopularReviewService.addPopularScore(review, TodayPopularReviewService.POPULAR_LIKE_SCORE);
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
