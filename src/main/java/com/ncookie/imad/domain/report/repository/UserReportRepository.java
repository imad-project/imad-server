package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
