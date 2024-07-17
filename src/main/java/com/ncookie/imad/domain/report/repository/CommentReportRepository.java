package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
