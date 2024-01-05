package com.ncookie.imad.domain.ranking.dto.response;

import com.ncookie.imad.domain.ranking.dto.ContentsData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingInfo {
    Set<ContentsData> contentsDataSet;

    public static RankingInfo toDTO(Set<ContentsData> contentsDataSet) {
        return RankingInfo.builder()
                .contentsDataSet(contentsDataSet)
                .build();
    }
}
