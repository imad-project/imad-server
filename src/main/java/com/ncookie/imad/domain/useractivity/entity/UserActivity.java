package com.ncookie.imad.domain.useractivity.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.review.entity.Review;
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
@Entity
public class UserActivity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_activity_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id", nullable = false)
    @ToString.Exclude
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bookmark_id")
    @ToString.Exclude
    private ContentsBookmark contentsBookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    @ToString.Exclude
    private Posting posting;

    // 활동 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter private ActivityType activityType;
}
