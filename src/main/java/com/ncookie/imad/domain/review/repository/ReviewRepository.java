package com.ncookie.imad.domain.review.repository;

import com.ncookie.imad.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}