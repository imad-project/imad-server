package com.ncookie.imad.domain.posting.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.common.dto.ListResponse;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;


@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListResponse extends ListResponse<CommentDetailsResponse> {
    public static CommentListResponse toDTO(Page<?> page, List<CommentDetailsResponse> commentList) {
        SortVariable sortVariable = getSortVariable(page);
        return CommentListResponse.builder()
                .detailsList(commentList)

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
