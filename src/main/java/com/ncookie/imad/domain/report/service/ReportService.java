package com.ncookie.imad.domain.report.service;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.service.CommentRetrievalService;
import com.ncookie.imad.domain.posting.service.PostingRetrievalService;
import com.ncookie.imad.domain.report.dto.request.ReportRequest;
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

    public void reportUser(String accessToken, ReportRequest reportRequest) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        UserAccount reportedUser = userRetrievalService.getUserById(reportRequest.getReportedId());
        if (reportedUser == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_USER);
        }

        validateSelfReport(reporter, reportRequest.getReportedId());

        // 유저 중복 신고 검사
        if (userReportRepository.findByReporterAndReportedUser(reporter, reportedUser).isPresent()) {
            throw new BadRequestException(ResponseCode.REPORT_ALREADY_REPORTED);
        }

        userReportRepository.save(
            UserReport.builder()
                    .reporter(reporter)
                    .reportedUser(reportedUser)
                    .reportType(ReportType.valueOf(reportRequest.getReportTypeString()))
                    .reportDesc(reportRequest.getReportDesc())
                    .build()
        );
    }

    public void reportReview(String accessToken, ReportRequest reportRequest) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Review reportedReview = reviewRetrievalService.findByReviewId(reportRequest.getReportedId());
        if (reportedReview == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedReview.getUserAccount().getId());

        // 리뷰 중복 신고 검사
        if (reviewReportRepository.findByReporterAndReportedReview(reporter, reportedReview).isPresent()) {
            throw new BadRequestException(ResponseCode.REPORT_ALREADY_REPORTED);
        }

        reviewReportRepository.save(
                ReviewReport.builder()
                        .reporter(reporter)
                        .reportedReview(reportedReview)
                        .reportType(ReportType.valueOf(reportRequest.getReportTypeString()))
                        .reportDesc(reportRequest.getReportDesc())
                        .build()
        );
    }

    public void reportPosting(String accessToken, ReportRequest reportRequest) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Posting reportedPosting = postingRetrievalService.getPostingEntityById(reportRequest.getReportedId());
        if (reportedPosting == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedPosting.getUser().getId());

        if (postingReportRepository.findByReporterAndReportedPosting(reporter, reportedPosting).isPresent()) {
            throw new BadRequestException(ResponseCode.REPORT_ALREADY_REPORTED);
        }

        postingReportRepository.save(
                PostingReport.builder()
                        .reporter(reporter)
                        .reportedPosting(reportedPosting)
                        .reportType(ReportType.valueOf(reportRequest.getReportTypeString()))
                        .reportDesc(reportRequest.getReportDesc())
                        .build()
        );
    }

    public void reportComment(String accessToken, ReportRequest reportRequest) {
        UserAccount reporter = userRetrievalService.getUserFromAccessToken(accessToken);

        Comment reportedComment = commentRetrievalService.getCommentById(reportRequest.getReportedId());
        if (reportedComment == null) {
            throw new BadRequestException(ResponseCode.REPORT_NOT_FOUND_CONTENTS);
        }

        validateSelfReport(reporter, reportedComment.getUserAccount().getId());

        if (commentReportRepository.findByReporterAndReportedComment(reporter, reportedComment).isPresent()) {
            throw new BadRequestException(ResponseCode.REPORT_ALREADY_REPORTED);
        }

        commentReportRepository.save(
                CommentReport.builder()
                        .reporter(reporter)
                        .reportedComment(reportedComment)
                        .reportType(ReportType.valueOf(reportRequest.getReportTypeString()))
                        .reportDesc(reportRequest.getReportDesc())
                        .build()
        );
    }

    public boolean isUserReported(UserAccount reporter, UserAccount reportedUser) {
        return userReportRepository.existsByReporterAndReportedUser(reporter, reportedUser);
    }

    // 리뷰 신고 여부 확인
    public boolean isReviewReported(UserAccount reporter, Review review) {
        return reviewReportRepository.existsByReporterAndReportedReview(reporter, review);
    }

    // 게시글 신고 여부 확인
    public boolean isPostingReported(UserAccount reporter, Posting posting) {
        return postingReportRepository.existsByReporterAndReportedPosting(reporter, posting);
    }

    // 댓글 신고 여부 확인
    public boolean isCommentReported(UserAccount reporter, Comment comment) {
        return commentReportRepository.existsByReporterAndReportedComment(reporter, comment);
    }

    // 셀프 신고 확인
    private void validateSelfReport(UserAccount reporter, Long reportedUserId) {
        if (Objects.equals(reporter.getId(), reportedUserId)) {
            throw new BadRequestException(ResponseCode.REPORT_CANNOT_SELF_REPORT);
        }
    }
}
