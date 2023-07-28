package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AllTimeRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allTimeRankingId;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;

    @Setter
    private int allTimeRank;

    @Setter
    private int rankChanged;
}
