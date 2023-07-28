package com.ncookie.imad.domain.review.repository;

import com.ncookie.imad.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
}