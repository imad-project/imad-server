package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.repository.ContentsRepository;
import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;
    private final ContentsRepository contentsRepository;

    // 작품 검색
    public SearchResponse searchKeywords(String query, String type, int page) {
        return apiClient.searchByQuery(query, type, page);
    }

    public void getContentsDetails(int id, String type) {
        contentsRepository.save(MovieData.builder()
                .contentsType(ContentsType.MOVIE).imadScore(50).runtime(100)
                .productionCountries(Arrays.asList("a", "b", "c")).build());
        contentsRepository.save(MovieData.builder()
                .contentsType(ContentsType.MOVIE).imadScore(60).runtime(110)
                .productionCountries(Arrays.asList("d", "e", "f")).build());

        contentsRepository.save(TvProgramData.builder()
                .contentsType(ContentsType.TV).imadScore(70).firstAirDate(LocalDate.of(2011, 8, 10))
                .productionCountries(Arrays.asList("aaa", "bbb", "ccc")).build());
        contentsRepository.save(TvProgramData.builder()
                .contentsType(ContentsType.TV).imadScore(70).firstAirDate(LocalDate.parse("2023-04-05"))
                .productionCountries(Arrays.asList("ddd", "eee", "fff")).build());

        apiClient.getContentsDetails(id, type);
    }
}
