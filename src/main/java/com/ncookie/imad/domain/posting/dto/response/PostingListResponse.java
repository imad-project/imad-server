package com.ncookie.imad.domain.posting.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;


@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostingListResponse {
    List<PostingDetailsResponse> postingDetailsResponseList;

    long totalElements;              // 총 리뷰 개수
    long totalPages;                 // 총 페이지 수

    int pageNumber;                 // 현재 페이지
    int numberOfElements;           // 현재 페이지의 리뷰 개수
    int sizeOfPage;                 // 한 페이지 당 최대 리뷰 개수

    int sortDirection;              // 0 : 오름차순, 1 : 내림차순
    String sortProperty;            // 정렬 기준 (createdDate, likeCnt, dislikeCnt 등이 있음)

    int searchType;                 // 검색 기준 (제목+내용, 제목, 내용, 글쓴이 등)

    public static PostingListResponse toDTO(Page<?> page, List<PostingDetailsResponse> postingList) {
        String sortProperty = null;
        int sortDirection = 0;
        Sort sort = page.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection().name().equals("ASC") ? 0 : 1;
        }

        return PostingListResponse.builder()
                .postingDetailsResponseList(postingList)

                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())

                .pageNumber(page.getNumber())
                .numberOfElements(page.getNumberOfElements())
                .sizeOfPage(page.getSize())

                .sortDirection(sortDirection)
                .sortProperty(sortProperty)

                .build();
    }
}
