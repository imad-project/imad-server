package com.ncookie.imad.domain.review.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.common.dto.ListResponse;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;


@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewListResponse extends ListResponse<ReviewDetailsResponse> {
    public static ReviewListResponse toDTO(Page<?> page, List<ReviewDetailsResponse> reviewList) {
        SortVariable sortVariable = getSortVariable(page);
        return ReviewListResponse.builder()
                .detailsList(reviewList)

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
