package com.ncookie.imad.domain.report.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportRequest {
    Long reportedId;            // 신고 대상 (유저, 리뷰, 게시글, 댓글 등)
    String reportTypeString;    // 신고 사유
    String reportDesc;          // 신고 상세 내용 (신고 사유로 기타를 선택했을 때 사용)
}
