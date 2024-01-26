package com.ncookie.imad.domain.ranking.dto;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingScoreDTO {
    private Contents contents;
    private ContentsType contentsType;

    private Long rank;
    private Long rankChanged;
    private Long rankingScore;
}
