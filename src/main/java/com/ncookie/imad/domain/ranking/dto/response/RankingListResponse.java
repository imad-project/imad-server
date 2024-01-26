package com.ncookie.imad.domain.ranking.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.common.dto.ListResponse;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;


@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RankingListResponse extends ListResponse<RankingDetailsResponse> {
    public static RankingListResponse toDTO(Page<?> page,
                                            List<RankingDetailsResponse> rankingDetailsResponseList) {
        SortVariable sortVariable = getSortVariable(page);
        return RankingListResponse.builder()
                .detailsList(rankingDetailsResponseList)

                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())

                .pageNumber(page.getNumber())
                .numberOfElements(page.getNumberOfElements())
                .sizeOfPage(page.getSize())

                .sortDirection(sortVariable.getSortDirection())
                .sortProperty(sortVariable.getSortProperty())

                .build();
    }
}
