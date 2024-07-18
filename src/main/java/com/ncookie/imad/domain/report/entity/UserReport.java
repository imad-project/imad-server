package com.ncookie.imad.domain.report.entity;

import com.ncookie.imad.domain.report.type.ReportType;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report_user")
@Entity
public class UserReport extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reporter_id")
    @ToString.Exclude
    private UserAccount reporter;   // 신고한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reported_user_id")
    @ToString.Exclude
    private UserAccount reportedUser;   // 신고 당한 유저

    private ReportType reportType;      // 신고 사유
    private String reportDesc;          // 신고 사유 기타 선택 시 상세 내용
}
