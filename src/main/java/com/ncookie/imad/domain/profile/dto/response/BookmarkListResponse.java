package com.ncookie.imad.domain.profile.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.common.dto.ListResponse;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;


@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookmarkListResponse extends ListResponse<BookmarkDetails> {
    public static BookmarkListResponse toDTO(Page<ContentsBookmark> page, List<BookmarkDetails> bookmarkDetailsList) {
        SortVariable sortVariable = getSortVariable(page);
        return BookmarkListResponse.builder()
                .detailsList(bookmarkDetailsList)

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
