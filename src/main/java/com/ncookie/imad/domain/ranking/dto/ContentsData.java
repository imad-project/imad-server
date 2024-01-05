package com.ncookie.imad.domain.ranking.dto;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentsData implements Serializable {
    private Long contentsId;
    private ContentsType contentsType;

    private String title;
    private String posterPath;

    // 어제자 데이터와의 랭킹 차이. 어제자 랭킹에 없는 작품인 경우 NULL값이 들어감
    private Long rankChanged;
    private Long rank;

    public static ContentsData from(Contents contents) {
        return ContentsData.builder()
                .contentsId(contents.getContentsId())
                .contentsType(contents.getContentsType())

                .title(contents.getTranslatedTitle())
                .posterPath(contents.getPosterPath())

                .rankChanged(null)
                .rank(null)
                .build();
    }
}
