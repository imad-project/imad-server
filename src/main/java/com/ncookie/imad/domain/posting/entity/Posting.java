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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;

    @Setter
    private int category;

    @Setter
    private String title;

    @Setter
    // 본문
    private String content;


    @Setter
    // 스포일러 여부
    private boolean isSpoiler;

    @Setter
    private int viewCnt;

    @Setter
    private int commentCnt;

    @Setter
    private int likeCnt;

    @Setter
    private int dislikeCnt;
}
