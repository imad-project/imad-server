package com.ncookie.imad.domain.review.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByContents(Contents contents, Pageable pageable);
    Page<Review> findAllByUserAccount(UserAccount userAccount, Pageable pageable);
    Review findByContentsAndUserAccount(Contents contents, UserAccount userAccount);

    boolean existsReviewByUserAccountAndContents(UserAccount user, Contents contents);

    @Query("SELECT AVG(score) FROM Review WHERE contents = :contents")
    Float calculateAverageScoreForContents(@Param("contents") Contents contents);

    @Query("SELECT COUNT(*) FROM Review WHERE contents = :contents")
    Integer countReviewForContents(@Param("contents") Contents contents);

    // 프로필 조회용
    @Query("SELECT COUNT(*) FROM Review WHERE userAccount = :user")
    int countWrittenReviewByUser(@Param("user") UserAccount user);

    // 좋아요 개수 업데이트
    @Modifying
    @Query("UPDATE Review SET likeCnt = :likeCnt WHERE reviewId = :reviewId")
    void updateLikeCount(@Param("reviewId") Long reviewId, @Param("likeCnt") int likeCnt);

    // 싫어요 개수 업데이트
    @Modifying
    @Query("UPDATE Review SET dislikeCnt = :dislikeCnt WHERE reviewId = :reviewId")
    void updateDislikeCount(@Param("reviewId") Long reviewId, @Param("dislikeCnt") int dislikeCnt);

    // 인기 리뷰 조회용
    @Query("SELECT r FROM Review r " +
            "WHERE r.likeCnt = (SELECT MAX(r2.likeCnt) FROM Review r2)")
    List<Review> findMostLikeReview();
}
