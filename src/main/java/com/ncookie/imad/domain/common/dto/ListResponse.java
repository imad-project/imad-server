package com.ncookie.imad.domain.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;


@Data
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ListResponse<T> {
    private List<T> detailsList;

    private long totalElements;              // 총 스크랩 개수
    private long totalPages;                 // 총 페이지 수

    private int pageNumber;                 // 현재 페이지
    private int numberOfElements;           // 현재 페이지의 스크랩 개수
    private int sizeOfPage;                 // 한 페이지 당 최대 스크랩 개수

    private int sortDirection;              // 0 : 오름차순, 1 : 내림차순
    private String sortProperty;            // 정렬 기준 (createdDate)


    @Getter
    @AllArgsConstructor
    public static class SortVariable {
        private int sortDirection;
        private String sortProperty;
    }

    protected static SortVariable getSortVariable(Page<?> page) {
        int sortDirection = 0;
        String sortProperty = null;
        Sort sort = page.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortDirection = order.getDirection().isAscending() ? 0 : 1;
            sortProperty = order.getProperty();
        }

        return new SortVariable(sortDirection, sortProperty);
    }
}
