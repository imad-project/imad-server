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
@Description("유저의 게시글별 북마크/스크랩 상태 저장")
public class PostingBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingBookmarkId;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Setter
    private int bookmarkStatus;
}
