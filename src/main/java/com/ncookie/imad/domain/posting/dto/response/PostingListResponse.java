package com.ncookie.imad.domain.posting.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.common.dto.ListResponse;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;


@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostingListResponse extends ListResponse<PostingListElement> {
    private Integer searchType;                 // 검색 기준 (제목+내용, 제목, 내용, 글쓴이 등)

    public static PostingListResponse toDTO(Page<?> page, List<PostingListElement> postingList, Integer searchType) {
        SortVariable sortVariable = getSortVariable(page);
        return PostingListResponse.builder()
                .detailsList(postingList)

                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())

                .pageNumber(page.getNumber())
                .numberOfElements(page.getNumberOfElements())
                .sizeOfPage(page.getSize())

                .sortDirection(sortVariable.getSortDirection())
                .sortProperty(sortVariable.getSortProperty())

                .searchType(searchType)

                .build();
    }
}
