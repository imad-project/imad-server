package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;

    public void searchKeywords() {
        apiClient.searchMultiByQuery("귀멸");
    }
}
