package com.ncookie.imad.domain.tmdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.tmdb.dto.DetailsGenre;
import com.ncookie.imad.domain.tmdb.dto.DetailsMovie;
import com.ncookie.imad.domain.tmdb.dto.DetailsTv;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TmdbDetailsSavingService {
    public final ContentsService contentsService;

    @Transactional
    public void saveContentsDetails(String detailsJsonData, String type) {
        // TODO: Contents(MovieData, TvProgramData), Networks, Season, Person Entity 저장
        /*
         * JSON 데이터를 분리해야 함
         * builder로 각 DTO 생성해주고, 해당하는 도메인의 service에 값을 전달하여 DB에 저장하도록 해야함
         * 이를 위해 도메인 별 서비스에 repository save 메소드 추가
         *
         * DTO 클래스를 구현한 builder 객체를 연결 테이블에 할당하여 관계 구축
         */

        ObjectMapper objectMapper = new ObjectMapper();
        Set<Integer> contentsGenre = new HashSet<>();
        Set<String> productionCountries = new HashSet<>();

        try {
            if (type.equals("tv")) {
                DetailsTv detailsTv = objectMapper.readValue(detailsJsonData, DetailsTv.class);

            } else if (type.equals("movie")) {
                DetailsMovie movieDataJsonObject = objectMapper.readValue(detailsJsonData, DetailsMovie.class);
                ContentsType contentsType =
                        extractAndCheckAnimationGenre(movieDataJsonObject.getGenres(), contentsGenre) ? ContentsType.ANIMATION : ContentsType.MOVIE;
                movieDataJsonObject.getProductionCountries().forEach(country -> productionCountries.add(country.getName()));

                MovieData movieData = MovieData.builder()
                        .tmdbId(movieDataJsonObject.getId())
                        .contentsType(contentsType)
                        .contentsGenres(contentsGenre)

                        .originalTitle(movieDataJsonObject.getOriginalTitle())
                        .originalLanguage(movieDataJsonObject.getOriginalLanguage())
                        .translatedTitle(movieDataJsonObject.getTitle())

                        .overview(movieDataJsonObject.getOverview())
                        .tagline(movieDataJsonObject.getTagline())
                        .posterPath(movieDataJsonObject.getPosterPath())
                        .productionCountries(productionCountries)

                        // MOVIE 고유 데이터
                        .releaseDate(LocalDate.parse(movieDataJsonObject.getReleaseDate()))
                        .releaseStatus(movieDataJsonObject.getStatus().equals("Released"))
                        .runtime(movieDataJsonObject.getRuntime())

                        .build();

                contentsService.saveMovieData(movieData);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean extractAndCheckAnimationGenre(Set<DetailsGenre> genres, Set<Integer> contentsGenre) {
        boolean isAnimation = false;
        for (DetailsGenre genre : genres) {
            contentsGenre.add(genre.getId());
            
            // 장르에 애니메이션이 포함된 경우
            if (genre.getId() == 16) {
                isAnimation = true;
            }
        }

        return isAnimation;
    }
}
