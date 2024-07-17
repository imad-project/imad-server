package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.report.entity.UserReport;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    Optional<UserReport> findByReporterAndReportedUser(UserAccount reporter, UserAccount reportedUser);
}
