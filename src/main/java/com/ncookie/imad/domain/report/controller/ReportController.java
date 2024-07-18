package com.ncookie.imad.domain.report.controller;

import com.ncookie.imad.domain.recommend.dto.response.AllRecommendationResponse;
import com.ncookie.imad.domain.report.dto.request.ReportRequest;
import com.ncookie.imad.domain.report.service.ReportService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @Description("유저 신고")
    @PostMapping("/user")
    public ApiResponse<?> reportUser(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody ReportRequest reportRequest) {
        reportService.reportUser(accessToken, reportRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REPORT_REQUEST_SUCCESS);
    }

    @Description("리뷰 신고")
    @PostMapping("/review")
    public ApiResponse<?> reportReview(@RequestHeader("Authorization") String accessToken,
                                       @RequestBody ReportRequest reportRequest) {
        reportService.reportReview(accessToken, reportRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REPORT_REQUEST_SUCCESS);
    }

    @Description("게시글 신고")
    @PostMapping("/posting")
    public ApiResponse<?> reportPosting(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody ReportRequest reportRequest) {
        reportService.reportPosting(accessToken, reportRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REPORT_REQUEST_SUCCESS);
    }

    @Description("댓글 신고")
    @PostMapping("/comment")
    public ApiResponse<?> reportComment(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody ReportRequest reportRequest) {
        reportService.reportComment(accessToken, reportRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REPORT_REQUEST_SUCCESS);
    }
}
