package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.ReviewReport;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    Optional<ReviewReport> findByReporterAndReportedReview(UserAccount reporter, Review reportedReview);
}
