package com.ncookie.imad.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScrapListResponse {
    private List<ScrapDetails> scrapDetailsList;
    
    long totalElements;              // 총 스크랩 개수
    long totalPages;                 // 총 페이지 수

    int pageNumber;                 // 현재 페이지
    int numberOfElements;           // 현재 페이지의 스크랩 개수
    int sizeOfPage;                 // 한 페이지 당 최대 스크랩 개수

    int sortDirection;              // 0 : 오름차순, 1 : 내림차순
    String sortProperty;            // 정렬 기준 (createdDate)

    public static ScrapListResponse toDTO(Page<PostingScrap> scrapPage, List<ScrapDetails> scrapDetailsList) {
        String sortProperty = null;
        int sortDirection = 0;
        Sort sort = scrapPage.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection().name().equals("ASC") ? 0 : 1;
        }

        return ScrapListResponse.builder()
                .scrapDetailsList(scrapDetailsList)

                .totalElements(scrapPage.getTotalElements())
                .totalPages(scrapPage.getTotalPages())

                .pageNumber(scrapPage.getNumber())
                .numberOfElements(scrapPage.getNumberOfElements())
                .sizeOfPage(scrapPage.getSize())

                .sortDirection(sortDirection)
                .sortProperty(sortProperty)

                .build();
    }
}
