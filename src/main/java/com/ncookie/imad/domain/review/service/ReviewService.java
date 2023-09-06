package com.ncookie.imad.domain.review.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.review.dto.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.dto.AddReviewRequest;
import com.ncookie.imad.domain.review.dto.AddReviewResponse;
import com.ncookie.imad.domain.review.dto.ModifyReviewRequest;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review.repository.ReviewRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserAccountService userAccountService;
    private final ContentsService contentsService;


    public ReviewDetailsResponse getReview(Long reviewId) {
        Optional<Review> optional = reviewRepository.findById(reviewId);

        if (optional.isPresent()) {
            Review review = optional.get();
            return ReviewDetailsResponse.builder()
                    .reviewId(review.getReviewId())
                    .contentsId(review.getContents().getContentsId())

                    .title(review.getTitle())
                    .content(review.getContent())

                    .score(review.getScore())
                    .isSpoiler(review.isSpoiler())

                    .likeCnt(review.getLikeCnt())
                    .dislikeCnt(review.getDislikeCnt())

                    .createdAt(review.getCreatedDate())
                    .modifiedAt(review.getModifiedDate())

                    .build();
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    public AddReviewResponse addReview(String accessToken, AddReviewRequest addReviewRequest) {
        UserAccount user = getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(addReviewRequest.getContentsId());

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

        return AddReviewResponse.builder()
                .reviewId(review.getReviewId())
                .build();
    }

    public Long modifyReview(String accessToken, Long reviewId, ModifyReviewRequest reviewRequest) {
        Optional<Review> optional = reviewRepository.findById(reviewId);
        UserAccount user = getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Review review = optional.get();

            // 해당 리뷰를 작성한 유저만 수정할 수 있음
            if (Objects.equals(review.getUserAccount().getId(), user.getId())) {
                review.setTitle(reviewRequest.getTitle());
                review.setContent(reviewRequest.getContent());
                review.setScore(reviewRequest.getScore());
                review.setSpoiler(reviewRequest.isSpoiler());

                return reviewRepository.save(review).getReviewId();
            } else {
                throw new BadRequestException(ResponseCode.REVIEW_MODIFY_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_NOT_FOUND);
        }
    }

    // 유저 null 체크를 위한 공용 메소드
    private UserAccount getUserFromAccessToken(String accessToken) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (user == null) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        } else {
            return user;
        }
    }
}
