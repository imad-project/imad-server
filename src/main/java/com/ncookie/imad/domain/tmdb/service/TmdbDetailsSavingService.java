package com.ncookie.imad.domain.tmdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.season.dto.DetailsSeason;
import com.ncookie.imad.domain.season.entity.Season;
import com.ncookie.imad.domain.season.service.SeasonService;
import com.ncookie.imad.domain.tmdb.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Slf4j
@Service
public class TmdbDetailsSavingService {
    private final ContentsService contentsService;
    private final SeasonService seasonService;

    @Transactional
    public TmdbDetails saveContentsDetails(String detailsJsonData, String type, String certification) {
        // TODO: Networks, Season, Person Entity 저장
        // TODO: tmdb id와 type 사용하여 데이터베이스 중복 검사 선행
        /*
         * JSON 데이터를 분리해야 함
         * builder로 각 DTO 생성해주고, 해당하는 도메인의 service에 값을 전달하여 DB에 저장하도록 해야함
         * 이를 위해 도메인 별 서비스에 repository save 메소드 추가
         *
         * DTO 클래스를 구현한 builder 객체를 연결 테이블에 할당하여 관계 구축
         */
        // DB 변경점 : 장르 관련 테이블 제거

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TmdbDetails tmdbDetails = objectMapper.readValue(detailsJsonData, TmdbDetails.class);

            // 애니메이션 장르가 포함되어 있으면 contents_type을 "ANIMATION"으로 설정
            ContentsType contentsType = checkAnimationGenre(tmdbDetails.getGenres(), type);

            tmdbDetails.setContentsType(contentsType);
            tmdbDetails.setCertification(certification);

            if (type.equals("tv")) {

                // TV 데이터 DB 저장 및 contetns_id 설정
                log.info("TV 데이터 저장 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getName());
                TvProgramData savedTvProgramData = contentsService.saveTvData(
                        TvProgramData.builder()
                                .tmdbId(tmdbDetails.getTmdbId())
                                .contentsType(contentsType)
                                .contentsGenres(tmdbDetails.getGenres())
                                .certification(tmdbDetails.getCertification())

                                .originalTitle(tmdbDetails.getOriginalName())
                                .originalLanguage(tmdbDetails.getOriginalLanguage())
                                .translatedTitle(tmdbDetails.getName())

                                .overview(tmdbDetails.getOverview())
                                .tagline(tmdbDetails.getTagline())
                                .posterPath(tmdbDetails.getPosterPath())
                                .productionCountries(tmdbDetails.getProductionCountries())

                                // TV 고유 데이터
                                .firstAirDate(LocalDate.parse(tmdbDetails.getFirstAirDate()))
                                .lastAirDate(LocalDate.parse(tmdbDetails.getLastAirDate()))
                                .numberOfEpisodes(tmdbDetails.getNumberOfEpisodes())
                                .numberOfSeasons(tmdbDetails.getNumberOfSeasons())

                                .build()
                );
                tmdbDetails.setContentsId(savedTvProgramData.getContentsId());

                // 시즌 데이터 DB 저장
                log.info("SEASON 정보 DB 저장 시작");

                List<DetailsSeason> seasons = tmdbDetails.getSeasons();
                for (DetailsSeason s : seasons) {
                    Season seasonBuild = Season.builder()
                            .seasonId(s.getId())
                            .seasonName(s.getName())
                            .airDate(s.getAirDate())
                            .episodeCount(s.getEpisodeCount())
                            .overview(s.getOverview())
                            .posterPath(s.getPosterPath())
                            .seasonNumber(s.getSeasonNumber())
                            .build();

                    Season savedSeason = seasonService.saveSeasonInfo(seasonBuild);
                    seasonService.saveSeasonCollection(savedSeason, savedTvProgramData);
                }

                log.info("SEASON 정보 DB 저장 완료");
            } else if (type.equals("movie")) {

                // MOVIE 데이터 DB 저장 및 contetns_id 설정
                log.info("MOVIE 데이터 저장 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getTitle());
                tmdbDetails.setContentsId(contentsService.saveMovieData(
                        MovieData.builder()
                                .tmdbId(tmdbDetails.getTmdbId())
                                .contentsType(contentsType)
                                .contentsGenres(tmdbDetails.getGenres())
                                .certification(tmdbDetails.getCertification())

                                .originalTitle(tmdbDetails.getOriginalTitle())
                                .originalLanguage(tmdbDetails.getOriginalLanguage())
                                .translatedTitle(tmdbDetails.getTitle())

                                .overview(tmdbDetails.getOverview())
                                .tagline(tmdbDetails.getTagline())
                                .posterPath(tmdbDetails.getPosterPath())
                                .productionCountries(tmdbDetails.getProductionCountries())

                                // MOVIE 고유 데이터
                                .releaseDate(LocalDate.parse(tmdbDetails.getReleaseDate()))
                                .releaseStatus(tmdbDetails.getStatus().equals("Released"))
                                .runtime(tmdbDetails.getRuntime())

                                .build()
                ));
            }

            log.info("TMDB API details 및 credits 정보 DB 저장 완료");
            return tmdbDetails;

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
