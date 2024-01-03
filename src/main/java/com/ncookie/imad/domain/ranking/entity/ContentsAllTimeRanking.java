package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
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
public class ContentsAllTimeRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allTimeRankingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private Contents contents;

    @Setter
    private int allTimeRank;

    @Setter
    private int rankChanged;
}