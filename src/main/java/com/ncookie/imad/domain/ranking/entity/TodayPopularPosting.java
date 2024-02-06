package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.posting.entity.Posting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodayPopularPosting {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "posting_id")
    @ToString.Exclude
    private Posting posting;
    
    // 인기 게시글 점수
    @Setter
    private Long popularScore;
}
