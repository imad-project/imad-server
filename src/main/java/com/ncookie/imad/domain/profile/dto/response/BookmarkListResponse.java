package com.ncookie.imad.domain.profile.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookmarkListResponse {
    private List<BookmarkDetails> bookmarkDetailsList;
    
    long totalElements;              // 총 북마크 개수
    long totalPages;                 // 총 페이지 수

    int pageNumber;                 // 현재 페이지
    int numberOfElements;           // 현재 페이지의 북마크 개수
    int sizeOfPage;                 // 한 페이지 당 최대 북마크 개수

    int sortDirection;              // 0 : 오름차순, 1 : 내림차순
    String sortProperty;            // 정렬 기준 (createdDate)

    public static BookmarkListResponse toDTO(Page<ContentsBookmark> contentsBookmarkPage, List<BookmarkDetails> bookmarkDetailsList) {
        String sortProperty = null;
        int sortDirection = 0;
        Sort sort = contentsBookmarkPage.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection().name().equals("ASC") ? 0 : 1;
        }

        return BookmarkListResponse.builder()
                .bookmarkDetailsList(bookmarkDetailsList)

                .totalElements(contentsBookmarkPage.getTotalElements())
                .totalPages(contentsBookmarkPage.getTotalPages())

                .pageNumber(contentsBookmarkPage.getNumber())
                .numberOfElements(contentsBookmarkPage.getNumberOfElements())
                .sizeOfPage(contentsBookmarkPage.getSize())

                .sortDirection(sortDirection)
                .sortProperty(sortProperty)

                .build();
    }
}
