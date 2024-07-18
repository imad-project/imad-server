package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.report.entity.PostingReport;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostingReportRepository extends JpaRepository<PostingReport, Long> {
    Optional<PostingReport> findByReporterAndReportedPosting(UserAccount reporter, Posting reportedPosting);
    boolean existsByReporterAndReportedPosting(UserAccount reporter, Posting reportedPosting);
}
