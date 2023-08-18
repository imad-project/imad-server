package com.ncookie.imad.domain.tmdb.service;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.networks.dto.DetailsNetworks;
import com.ncookie.imad.domain.networks.entity.Networks;
import com.ncookie.imad.domain.networks.service.NetworksService;
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
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
/**
 * 이 프로젝트에서는 되도록 service와 entity 간의 관계를 1대1로 유지하고 싶다.
 * 그리고 하나의 클래스에서 다른 클래스를 호출하는 것 자체는 문제 없지만, 순환참조 이슈를 신경써야 한다.
 *
 * 이를 위해 도메인의 entity와 1대1로 매칭되는 서비스들을 호출하여 도메인이 겹치는 작업을 수행하는 서비스를 만들었다.
 * 이 서비스는 컨트롤러에서만 호출되어야 하며, 순환참조 방지를 위해 다른 서비스에서는 호출되면 안 된다.
 * 또한 직접 repository를 참조하지 않도록 한다.
 */
public class TmdbService {
    private final ContentsService contentsService;

    private final SeasonService seasonService;
    private final NetworksService networksService;


    @Transactional
    public TmdbDetails getTmdbDetails(long id, ContentsType type) {
        TmdbDetails tmdbDetails = TmdbDetails.builder().build();

        if (type.equals(ContentsType.TV)) {
            TvProgramData tvProgramData = contentsService.getTvProgramDataByTmdbIdAndTmdbType(id, type);
            List<Season> seasonsEntities = seasonService.getSeasonsEntities(tvProgramData);
            List<Networks> networksEntities = networksService.getNetworksEntities(tvProgramData);

            tmdbDetails = TmdbDetails.builder()
                    .contentsId(tvProgramData.getContentsId())
                    .tmdbId(tvProgramData.getTmdbId())

                    .overview(tvProgramData.getOverview())
                    .tagline(tvProgramData.getTagline())
                    .posterPath(tvProgramData.getPosterPath())
                    .originalLanguage(tvProgramData.getOriginalLanguage())
                    .certification(tvProgramData.getCertification())
                    .status(tvProgramData.getStatus())

                    .genres(tvProgramData.getContentsGenres())
                    .productionCountries(tvProgramData.getProductionCountries())

                    .contentsType(tvProgramData.getContentsType())

                    .name(tvProgramData.getTranslatedTitle())
                    .originalName(tvProgramData.getOriginalTitle())
                    .firstAirDate(tvProgramData.getFirstAirDate().toString())
                    .lastAirDate(tvProgramData.getLastAirDate().toString())

                    .numberOfEpisodes(tvProgramData.getNumberOfEpisodes())
                    .numberOfSeasons(tvProgramData.getNumberOfSeasons())

                    .seasons(seasonsEntities.stream()
                            .map(DetailsSeason::toDTO)
                            .collect(Collectors.toList()))
                    .networks(networksEntities.stream()
                            .map(DetailsNetworks::toDTO)
                            .collect(Collectors.toList()))
                    .build();

        } else if (type.equals(ContentsType.MOVIE)) {
            MovieData movieData = contentsService.getMovieDataByTmdbIdAndTmdbType(id, type);

            tmdbDetails = TmdbDetails.builder()
                    .contentsId(movieData.getContentsId())
                    .tmdbId(movieData.getTmdbId())

                    .overview(movieData.getOverview())
                    .tagline(movieData.getTagline())
                    .posterPath(movieData.getPosterPath())
                    .originalLanguage(movieData.getOriginalLanguage())
                    .certification(movieData.getCertification())
                    .status(movieData.getStatus())

                    .genres(movieData.getContentsGenres())
                    .productionCountries(movieData.getProductionCountries())

                    .contentsType(movieData.getContentsType())

                    .title(movieData.getTranslatedTitle())
                    .originalTitle(movieData.getOriginalTitle())

                    .releaseDate(movieData.getReleaseDate().toString())
                    .runtime(movieData.getRuntime())

                    .build();
        }

        return tmdbDetails;
    }


    @Transactional
    public TmdbDetails saveAndGetContentsDetails(TmdbDetails tmdbDetails, String type, String certification) {
        // TODO: Person Entity 저장
        // TODO: TMDB 404 에러 발생했을 때 예외처리도 해줘야 함
        // DB 변경점 : 장르 관련 테이블 제거, Contents에 공통 필드로 status 추가

        try {
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
                                .tmdbType(ContentsType.TV)
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
                                .status(tmdbDetails.getStatus())

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

                    Season savedSeason = seasonService.saveSeasonInfo(Season.toEntity(s));
                    seasonService.saveSeasonCollection(savedSeason, savedTvProgramData);
                }

                log.info("SEASON 정보 DB 저장 완료");

                // 방송사 데이터 DB 저장
                List<DetailsNetworks> networks = tmdbDetails.getNetworks();
                for (DetailsNetworks n : networks) {
                    Networks savedNetworksInfo = networksService.saveNetworksInfo(Networks.toEntity(n));
                    networksService.saveBroadcaster(savedNetworksInfo, savedTvProgramData);
                }
            } else if (type.equals("movie")) {

                // MOVIE 데이터 DB 저장 및 contetns_id 설정
                log.info("MOVIE 데이터 저장 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getTitle());
                tmdbDetails.setContentsId(contentsService.saveMovieData(
                        MovieData.builder()
                                .tmdbId(tmdbDetails.getTmdbId())
                                .tmdbType(ContentsType.MOVIE)
                                .contentsType(contentsType)
                                .contentsGenres(tmdbDetails.getGenres())
                                .certification(tmdbDetails.getCertification())

                                .originalTitle(tmdbDetails.getOriginalTitle())
                                .originalLanguage(tmdbDetails.getOriginalLanguage())
                                .translatedTitle(tmdbDetails.getTitle())

                                .overview(tmdbDetails.getOverview())
                                .tagline(tmdbDetails.getTagline())
                                .posterPath(tmdbDetails.getPosterPath())
                                .status(tmdbDetails.getStatus())

                                .productionCountries(tmdbDetails.getProductionCountries())

                                // MOVIE 고유 데이터
                                .releaseDate(LocalDate.parse(tmdbDetails.getReleaseDate()))
                                .runtime(tmdbDetails.getRuntime())

                                .build()
                ));
            }

            log.info("TMDB API details 및 credits 정보 DB 저장 완료");
            return tmdbDetails;

        } catch (Exception e) {
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
