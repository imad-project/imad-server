package com.ncookie.imad.domain.report.dto.request;

// 신고 요청 시 사용하는 DTO 클래스
public record ReportRequest(
    Long reportedId,            // 신고 대상 (유저, 리뷰, 게시글, 댓글 등)
    String reportTypeString,    // 신고 사유
    String reportDesc           // 신고 상세 내용 (신고 사유로 기타를 선택했을 때 사용)
) {
    public static ReportRequest of(Long reportedId, String reportTypeString, String reportDesc) {
        return new ReportRequest(reportedId, reportTypeString, reportDesc);
    }
}
