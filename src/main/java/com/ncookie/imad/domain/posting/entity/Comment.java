package com.ncookie.imad.domain.posting.entity;

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
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;
    
    // 부모 댓글. 답글을 달았을 때 최상위 댓글 아래에 순차적으로 등록됨
    private Long parentId;

    @Setter
    private String content;
}
