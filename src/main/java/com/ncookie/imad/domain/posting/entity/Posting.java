package com.ncookie.imad.domain.posting.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
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
public class Posting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private Contents contents;

    @Setter
    private int category;

    @Setter
    private String title;

    @Column(length = 5000)
    @Setter
    private String content;         // 본문


    @Setter
    private boolean isSpoiler;      // 스포일러 여부

    @Setter
    private int viewCnt;

    @Setter
    private int commentCnt;

    @Setter
    private int likeCnt;

    @Setter
    private int dislikeCnt;
}
