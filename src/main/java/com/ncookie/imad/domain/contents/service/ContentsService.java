package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.global.openfeign.TmdbFeignClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbFeignClient feignClient;

    @Value("${tmdb.api.api-key}")
    private String apiKey;


    public void searchKeywords() {
        SearchResponse response = feignClient.searchMultiByQuery(
                apiKey,
                MediaType.APPLICATION_JSON_VALUE,
                "breaking",
                true,
                "ko-kr",
                1
        );
    }
}
