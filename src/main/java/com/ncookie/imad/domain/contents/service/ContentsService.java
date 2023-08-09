package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;

    // 작품 검색
    public SearchResponse searchKeywords(String query, String type, int page) {
        return apiClient.searchByQuery(query, type, page);
    }

    public void getContentsDetails(int id, String type) {
        apiClient.getContentsDetails(id, type);
    }
}
