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


    public static RankingDetailsResponse toDTO(RankingBaseEntity rankingBaseEntity, ContentsType contentsType) {
        Contents contents = rankingBaseEntity.getContents();

        Long ranking = 0L;
        Long rankingChanged = 0L;

        // 작품 타입에 따라 랭킹 데이터 반환
        switch (contentsType) {
            case ALL -> {
                ranking = rankingBaseEntity.getRanking();
                rankingChanged = rankingBaseEntity.getRankingChanged();
            }
            case TV -> {
                ranking = rankingBaseEntity.getRankingTv();
                rankingChanged = rankingBaseEntity.getRankingChangedTv();
            }
            case MOVIE -> {
                ranking = rankingBaseEntity.getRankingMovie();
                rankingChanged = rankingBaseEntity.getRankingChangedMovie();
            }
            case ANIMATION -> {
                ranking = rankingBaseEntity.getRankingAnimation();
                rankingChanged = rankingBaseEntity.getRankingChangedAnimation();
            }
        }

        return RankingDetailsResponse.builder()
                .contentsId(contents.getContentsId())
                .contentsType(contents.getContentsType())
                .imadScore(contents.getImadScore())

                .title(contents.getTranslatedTitle())
                .posterPath(contents.getPosterPath())

                .ranking(ranking)
                .rankingChanged(rankingChanged)

                .build();
    }
}
