package com.ncookie.imad.domain.review.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;


    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private float score;

    @Setter
    private boolean isSpoiler;

    @Setter
    private int likeCnt;

    @Setter
    private int dislikeCnt;
}
