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

    public String getContentsCertification(int id, String type) {
        return apiClient.getContentsCertification(id, type);
    }

    public long saveMovieData(MovieData movieData) {
        return movieDataRepository.save(movieData).getContentsId();
    }

    // SeasonCollection, Broadcast 같은 연결테이블에 데이터를 추가하기 위해서는 save 이후의 entity 객체가 필요함
    // 때문에 위의 saveMovieData() 메소드와 다르게 TvProgramData entity를 리턴함
    public TvProgramData saveTvData(TvProgramData tvProgramData) {
        return tvProgramDataRepository.save(tvProgramData);
    }
}
