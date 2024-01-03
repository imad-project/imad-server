package com.ncookie.imad.domain.ranking.dto;

import com.ncookie.imad.domain.contents.entity.Contents;
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

    private String title;
    private String posterPath;

    public static ContentsData toDTO(Contents contents) {
        return ContentsData.builder()
                .contentsId(contents.getContentsId())

                .title(contents.getTranslatedTitle())
                .posterPath(contents.getPosterPath())
                .build();
    }
}
