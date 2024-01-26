package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.ranking.dto.RankingScoreDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@SuperBuilder
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "ranking"),
        @Index(columnList = "rankingChanged")
})
public class RankingMonthly extends RankingBaseEntity {
    public static RankingMonthly toEntity(RankingScoreDTO dto) {
        return RankingMonthly.builder()

                .contents(dto.getContents())
                .contentsType(dto.getContentsType())

                .ranking(dto.getRank())
                .rankingChanged(dto.getRankChanged())
                .rankingScore(dto.getRankingScore())

                .build();
    }
}
