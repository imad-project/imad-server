package com.ncookie.imad.domain.report.service;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.service.CommentRetrievalService;
import com.ncookie.imad.domain.posting.service.PostingRetrievalService;
import com.ncookie.imad.domain.report.entity.CommentReport;
import com.ncookie.imad.domain.report.entity.PostingReport;
import com.ncookie.imad.domain.report.entity.ReviewReport;
import com.ncookie.imad.domain.report.entity.UserReport;
import com.ncookie.imad.domain.report.repository.CommentReportRepository;
import com.ncookie.imad.domain.report.repository.PostingReportRepository;
import com.ncookie.imad.domain.report.repository.ReviewReportRepository;
import com.ncookie.imad.domain.report.repository.UserReportRepository;
import com.ncookie.imad.domain.report.type.ReportType;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review.service.ReviewRetrievalService;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReportService {
    private final UserRetrievalService userRetrievalService;
    private final ReviewRetrievalService reviewRetrievalService;
    private final PostingRetrievalService postingRetrievalService;
    private final CommentRetrievalService commentRetrievalService;

    private final UserReportRepository userReportRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final PostingReportRepository postingReportRepository;
    private final CommentReportRepository commentReportRepository;

    /*
     * 신고 기능
     * - 유저는 신고 사유에 해당하는 컨텐츠를 신고할 수 있다.
     * - 신고된 컨텐츠는 해당 유저에게 더 이상 보이지 않는다.
     * - 유저는 자신의 계정이나 자신이 작성한 컨텐츠를 신고할 수 없다.
     * - 이미 신고한 컨텐츠는 중복으로 신고할 수 없다.
     * - 신고할 컨텐츠가 존재하는지 확인한다.
     */

    public void reportUser(String accessToken, Long reportedUserId, String reportTypeString, String reportDesc) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);
        validateSelfReport(reporter, reportedUserId);

        UserAccount reportedUser = userRetrievalService.getUserById(reportedUserId);
        if (reportedUser == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_USER);
        }

        userReportRepository.save(
            UserReport.builder()
                    .reporter(reporter)
                    .reportedUser(reportedUser)
                    .reportType(ReportType.valueOf(reportTypeString))
                    .reportDesc(reportDesc)
                    .build()
        );
    }

    public void reportReview(String accessToken, Long reportedReviewId, String reportTypeString, String reportDesc) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Review reportedReview = reviewRetrievalService.findByReviewId(reportedReviewId);
        if (reportedReview == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedReview.getUserAccount().getId());

        reviewReportRepository.save(
                ReviewReport.builder()
                        .reporter(reporter)
                        .reportedReview(reportedReview)
                        .reportType(ReportType.valueOf(reportTypeString))
                        .reportDesc(reportDesc)
                        .build()
        );
    }

    public void reportPosting(String accessToken, Long reportedPostingId, String reportTypeString, String reportDesc) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Posting reportedPosting = postingRetrievalService.getPostingEntityById(reportedPostingId);
        if (reportedPosting == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedPosting.getUser().getId());

        postingReportRepository.save(
                PostingReport.builder()
                        .reporter(reporter)
                        .reportedPosting(reportedPosting)
                        .reportType(ReportType.valueOf(reportTypeString))
                        .reportDesc(reportDesc)
                        .build()
        );
    }

    public void reportComment(String accessToken, Long reportedCommentId, String reportTypeString, String reportDesc) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Comment reportedComment = commentRetrievalService.getCommentById(reportedCommentId);
        if (reportedComment == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedComment.getUserAccount().getId());

        commentReportRepository.save(
                CommentReport.builder()
                        .reporter(reporter)
                        .reportedComment(reportedComment)
                        .reportType(ReportType.valueOf(reportTypeString))
                        .reportDesc(reportDesc)
                        .build()
        );
    }

    // 셀프 신고 확인
    private void validateSelfReport(UserAccount reporter, Long reportedUserId) {
        if (Objects.equals(reporter.getId(), reportedUserId)) {
            throw new BadRequestException(ResponseCode.REPORT_CANNOT_SELF_REPORT);
        }
    }
}
