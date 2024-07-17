package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.PostingReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingReportRepository extends JpaRepository<PostingReport, Long> {
}
