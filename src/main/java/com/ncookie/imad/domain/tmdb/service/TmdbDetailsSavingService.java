package com.ncookie.imad.domain.tmdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.tmdb.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TmdbDetailsSavingService {
    public final ContentsService contentsService;

    @Transactional
    public DetailsResponse saveContentsDetails(String detailsJsonData, String type, String certification) {
        // TODO: Contents(MovieData, TvProgramData), Networks, Season, Person Entity 저장
        /*
         * JSON 데이터를 분리해야 함
         * builder로 각 DTO 생성해주고, 해당하는 도메인의 service에 값을 전달하여 DB에 저장하도록 해야함
         * 이를 위해 도메인 별 서비스에 repository save 메소드 추가
         *
         * DTO 클래스를 구현한 builder 객체를 연결 테이블에 할당하여 관계 구축
         */
        // 장르 테이블 제거
        // TODO: certification 데이터 추가

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DetailsResponse detailsResponse = objectMapper.readValue(detailsJsonData, DetailsResponse.class);

            // 애니메이션 장르가 포함되어 있으면 contents_type을 "ANIMATION"으로 설정
            ContentsType contentsType = checkAnimationGenre(detailsResponse.getGenres(), type);

            detailsResponse.setContentsType(contentsType);
            detailsResponse.setCertification(certification);

            if (type.equals("tv")) {
                TvProgramData tvProgramData = TvProgramData.builder()
                        .tmdbId(detailsResponse.getId())
                        .contentsType(contentsType)
                        .contentsGenres(detailsResponse.getGenres())
                        .certification(detailsResponse.getCertification())

                        .originalTitle(detailsResponse.getOriginalName())
                        .originalLanguage(detailsResponse.getOriginalLanguage())
                        .translatedTitle(detailsResponse.getName())

                        .overview(detailsResponse.getOverview())
                        .tagline(detailsResponse.getTagline())
                        .posterPath(detailsResponse.getPosterPath())
                        .productionCountries(detailsResponse.getProductionCountries())

                        // TV 고유 데이터
                        .firstAirDate(LocalDate.parse(detailsResponse.getFirstAirDate()))
                        .lastAirDate(LocalDate.parse(detailsResponse.getLastAirDate()))
                        .numberOfEpisodes(detailsResponse.getNumberOfEpisodes())
                        .numberOfSeasons(detailsResponse.getNumberOfSeasons())

                        .build();

                contentsService.saveTvData(tvProgramData);
            } else if (type.equals("movie")) {
                MovieData movieData = MovieData.builder()
                        .tmdbId(detailsResponse.getId())
                        .contentsType(contentsType)
                        .contentsGenres(detailsResponse.getGenres())
                        .certification(detailsResponse.getCertification())

                        .originalTitle(detailsResponse.getOriginalTitle())
                        .originalLanguage(detailsResponse.getOriginalLanguage())
                        .translatedTitle(detailsResponse.getTitle())

                        .overview(detailsResponse.getOverview())
                        .tagline(detailsResponse.getTagline())
                        .posterPath(detailsResponse.getPosterPath())
                        .productionCountries(detailsResponse.getProductionCountries())

                        // MOVIE 고유 데이터
                        .releaseDate(LocalDate.parse(detailsResponse.getReleaseDate()))
                        .releaseStatus(detailsResponse.getStatus().equals("Released"))
                        .runtime(detailsResponse.getRuntime())

                        .build();

                contentsService.saveMovieData(movieData);
            }

            return detailsResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ContentsType checkAnimationGenre(Set<Integer> genres, String type) {
        if (genres.contains(16)) {
            return ContentsType.ANIMATION;
        } else {
            return type.equals("tv") ? ContentsType.TV : ContentsType.MOVIE;
        }
    }
}
