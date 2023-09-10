package com.ncookie.imad.domain.review.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsReviewByUserAccountAndContents(UserAccount user, Contents contents);

    @Query("SELECT AVG(score) FROM Review WHERE contents = :contents")
    Float calculateAverageScoreForContents(Contents contents);

    @Query("SELECT COUNT(*) FROM Review WHERE contents = :contents")
    Integer countReviewForContents(Contents contents);
}
