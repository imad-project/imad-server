package com.ncookie.imad.domain.report.type;

public enum ReportType {
    WRONG_INFO("WRONG_INFO"),                       // 잘못된 정보
    SPAM("SPAM"),                                   // 스팸, 상업적 광고
    ABUSIVE("ABUSIVE"),                             // 폭력적이거나 공격적인 내용
    INAPPROPRIATE("INAPPROPRIATE"),                 // 부적절한 내용 (성적인 컨텐츠, 혐오 발언 등)
    COPYRIGHT_VIOLATION("COPYRIGHT_VIOLATION"),     // 저작권 침해
    OTHER("OTHER");                                 // 기타

    private final String type;

    ReportType(String type) {
        this.type = type;
    }
}
