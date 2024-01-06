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
    Set<ContentsData> contentsDataSet;      // 랭킹 순으로 정렬되어 있는 랭킹 데이터

    public static RankingInfo toDTO(Set<ContentsData> contentsDataSet) {
        return RankingInfo.builder()
                .contentsDataSet(contentsDataSet)
                .build();
    }
}
