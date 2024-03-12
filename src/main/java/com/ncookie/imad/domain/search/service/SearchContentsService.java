package com.ncookie.imad.domain.search.service;

import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.search.dto.request.SearchContentsRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class SearchContentsService {
    private final ContentsService contentsService;

    public void searchTvProgramData(SearchContentsRequest searchContentsRequest) {
        contentsService.searchTvProgramData(searchContentsRequest);
    }
}
