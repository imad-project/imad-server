package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
}
