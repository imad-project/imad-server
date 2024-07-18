package com.ncookie.imad.domain.report.repository;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.report.entity.CommentReport;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    Optional<CommentReport> findByReporterAndReportedComment(UserAccount reporter, Comment reportedComment);
    boolean existsByReporterAndReportedComment(UserAccount reporter, Comment reportedComment);
}
