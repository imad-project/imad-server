package com.ncookie.imad.domain.ranking.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.entity.RankingBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RankingDetailsResponse {
    private Long contentsId;                    // contents ID
    private ContentsType contentsType;          // tv / movie / animation
    private Float imadScore;                    // 작품 평점

    private String title;                       // 제목
    private String posterPath;                  // 포스터

    // 현재 랭킹
    private Long ranking;

    // 어제자 데이터와의 랭킹 차이. 어제자 랭킹에 없는 작품인 경우 NULL값이 들어감
    private Long rankingChanged;


    public static RankingDetailsResponse toDTO(RankingBaseEntity rankingBaseEntity) {
        Contents contents = rankingBaseEntity.getContents();

        return RankingDetailsResponse.builder()
                .contentsId(contents.getContentsId())
                .contentsType(contents.getContentsType())
                .imadScore(contents.getImadScore())

                .title(contents.getTranslatedTitle())
                .posterPath(contents.getPosterPath())

                .ranking(rankingBaseEntity.getRanking())
                .rankingChanged(rankingBaseEntity.getRankingChanged())

                .build();
    }
}
