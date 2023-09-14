package com.ncookie.imad.domain.review.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.review.entity.Review;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewListResponse {
    // 여기서 보내는 ReviewDetailsResponse에는 userId와 likeStatus가 포함되어 있지 않고, 대신 null 또는 0 값이 들어간다.
    List<ReviewDetailsResponse> reviewDetailsResponseList;      // 리뷰 정보

    long totalElements;              // 총 리뷰 개수
    long totalPages;                 // 총 페이지 수

    int pageNumber;                 // 현재 페이지
    int numberOfElements;           // 현재 페이지의 리뷰 개수
    int sizeOfPage;                 // 한 페이지 당 최대 리뷰 개수

    int sortDirection;              // 0 : 오름차순, 1 : 내림차순
    String sortProperty;            // 정렬 기준 (score, createdDate, likeCnt, dislikeCnt 등이 있음)

    public static ReviewListResponse toDTO(Page<Review> reviewPage) {
        String sortProperty = null;
        int sortDirection = 0;
        Sort sort = reviewPage.getSort();
        List<Sort.Order> orders = sort.toList();
        for (Sort.Order order : orders) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection().name().equals("ASC") ? 0 : 1;
        }
        
        // Review 데이터 가공
        List<ReviewDetailsResponse> reviewList = new ArrayList<>();
        for (Review r : reviewPage.getContent().stream().toList()) {
            reviewList.add(ReviewDetailsResponse.toDTO(r));
        }

        return ReviewListResponse.builder()
                .reviewDetailsResponseList(reviewList)

                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())

                .pageNumber(reviewPage.getNumber())
                .numberOfElements(reviewPage.getNumberOfElements())
                .sizeOfPage(reviewPage.getSize())

                .sortDirection(sortDirection)
                .sortProperty(sortProperty)

                .build();
    }
}
