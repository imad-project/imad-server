package com.ncookie.imad.domain.like.entity;

import com.ncookie.imad.domain.posting.entity.Posting;
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
public class PostingLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingLikeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Posting posting;


    @Setter
    // 좋아요이면 +1, 싫어요이면 -1, 아무 상태도 아니면 해당 데이터 삭제
    private int likeStatus;
}
