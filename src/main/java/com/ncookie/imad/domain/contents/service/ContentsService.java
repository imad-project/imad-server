package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.repository.ContentsRepository;
import com.ncookie.imad.domain.contents.repository.MovieDataRepository;
import com.ncookie.imad.domain.contents.repository.TvProgramDataRepository;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;

    private final ContentsRepository contentsRepository;

    private final MovieDataRepository movieDataRepository;
    private final TvProgramDataRepository tvProgramDataRepository;

    // 작품 검색
    public ContentsSearchResponse searchKeywords(String query, String type, int page) {
        return apiClient.searchByQuery(query, type, page);
    }

    public TmdbDetails getContentsDetails(int id, String type) {
        return apiClient.getContentsDetails(id, type);
    }

    public String getContentsCertification(int id, String type) {
        return apiClient.getContentsCertification(id, type);
    }

    // 프로필에서 작품 북마크 관련으로 사용하는 메소드
    public Contents getContentsEntityById(Long id) {
        Optional<Contents> contents = contentsRepository.findById(id);

        if (contents.isPresent()) {
            return contents.get();
        } else {
            throw new BadRequestException(ResponseCode.CONTENTS_ID_NOT_FOUND);
        }
    }

    public MovieData saveMovieData(MovieData movieData) {
        return movieDataRepository.save(movieData);
    }

    // SeasonCollection, Broadcast 같은 연결테이블에 데이터를 추가하기 위해서는 save 이후의 entity 객체가 필요함
    // 때문에 위의 saveMovieData() 메소드와 다르게 TvProgramData entity를 리턴함
    public TvProgramData saveTvData(TvProgramData tvProgramData) {
        return tvProgramDataRepository.save(tvProgramData);
    }

    public boolean checkDuplicationExists(long id, ContentsType type) {
        return contentsRepository.existsByTmdbIdAndTmdbType(id, type);
    }

    public Contents getContentsByTmdbIdAndTmdbType(long id, ContentsType type) {
        return contentsRepository.findByTmdbIdAndTmdbType(id, type);
    }

    public TvProgramData getTvProgramDataByTmdbIdAndTmdbType(long id, ContentsType type) {
        return tvProgramDataRepository.findByTmdbIdAndTmdbType(id, type);
    }

    public MovieData getMovieDataByTmdbIdAndTmdbType(long id, ContentsType type) {
        return movieDataRepository.findByTmdbIdAndTmdbType(id, type);
    }


    // 리뷰 관련
    public void saveContentsScoreAndReviewCount(Contents contents) {
        contentsRepository.save(contents);
    }

}
