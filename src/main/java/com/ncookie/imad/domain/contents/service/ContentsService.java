package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.repository.MovieDataRepository;
import com.ncookie.imad.domain.contents.repository.TvProgramDataRepository;
import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;
    private final MovieDataRepository movieDataRepository;
    private final TvProgramDataRepository tvProgramDataRepository;

    // 작품 검색
    public SearchResponse searchKeywords(String query, String type, int page) {
        return apiClient.searchByQuery(query, type, page);
    }

    public String getContentsDetails(int id, String type) {
        return apiClient.getContentsDetails(id, type);
    }

    public void saveMovieData(MovieData movieData) {
        movieDataRepository.save(movieData);
    }

    public void saveTvData(TvProgramData tvProgramData) {
        tvProgramDataRepository.save(tvProgramData);
    }
}
