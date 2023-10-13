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

    @Setter
    private String content;

    @Setter
    private int commentOrder;
}
