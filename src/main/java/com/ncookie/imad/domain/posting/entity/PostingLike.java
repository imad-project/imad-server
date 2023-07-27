package com.ncookie.imad.domain.posting.entity;

import com.ncookie.imad.domain.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Description;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Description("유저의 게시글별 좋아요 상태 저장")
public class PostingLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingLikeId;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Setter
    private int likeStatus;
}
