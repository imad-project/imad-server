package com.ncookie.imad.domain.tmdb.service;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.networks.dto.DetailsNetworks;
import com.ncookie.imad.domain.networks.entity.Networks;
import com.ncookie.imad.domain.networks.service.NetworksService;
import com.ncookie.imad.domain.person.dto.DetailsPerson;
import com.ncookie.imad.domain.person.entity.CreditType;
import com.ncookie.imad.domain.person.entity.Person;
import com.ncookie.imad.domain.person.service.PersonService;
import com.ncookie.imad.domain.season.dto.DetailsSeason;
import com.ncookie.imad.domain.season.entity.Season;
import com.ncookie.imad.domain.season.service.SeasonService;
import com.ncookie.imad.domain.tmdb.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    private final PersonService personService;

    // 데이터 가공 시 캐스팅된 배우를 최대 몇 명까지 저장할지 지정
    private final int MAX_CAST_LIST_SIZE = 10;

    @Transactional
    public TmdbDetails getTmdbDetails(long id, ContentsType type) {
        TmdbDetails tmdbDetails = TmdbDetails.builder().build();

        if (type.equals(ContentsType.TV)) {
            log.info("TV 데이터를 DB로부터 조회 시작");
            TvProgramData tvProgramData = contentsService.getTvProgramDataByTmdbIdAndTmdbType(id, type);
            List<Season> seasonsEntities = seasonService.getSeasonsEntities(tvProgramData);
            List<Networks> networksEntities = networksService.getNetworksEntities(tvProgramData);
            log.info("TV 데이터를 DB로부터 조회 완료 : [" + tvProgramData.getContentsId() + "] " + tvProgramData.getTranslatedTitle());

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
            log.info("영화 데이터를 DB로부터 조회 시작");
            MovieData movieData = contentsService.getMovieDataByTmdbIdAndTmdbType(id, type);
            log.info("영화 데이터를 DB로부터 조회 완료 : [" + movieData.getContentsId() + "] " + movieData.getTranslatedTitle());

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
    public TmdbDetails saveContentsDetails(TmdbDetails tmdbDetails, ContentsType type, String certification) {
        // TODO: TMDB 404 에러 발생했을 때 예외처리도 해줘야 함
        // DB 변경점 : 장르 관련 테이블 제거, Contents에 공통 필드로 status 추가
        // 필모그래피에 credit_id 추가, Filmography -> Credit으로 테이블 이름 변경, TMDB에서 제공하는 String 형식의 기본키 사용

        // 애니메이션 장르가 포함되어 있으면 contents_type을 "ANIMATION"으로 설정
        ContentsType contentsType = checkAnimationGenre(tmdbDetails.getGenres(), type);

        tmdbDetails.setContentsType(contentsType);
        tmdbDetails.setCertification(certification);

        // repository save 이후 반환될 entity를 저장할 변수들 선언
        TvProgramData savedTvProgramData = null;
        MovieData savedMovieData = null;

        try {
            // credits 데이터 가공
            List<DetailsPerson> castList = getCastListWithMaximumSize(tmdbDetails.getCredits().getCast());
            List<DetailsPerson> crewList = mergeAndCleanDuplicateCrew(tmdbDetails.getCredits().getCrew());

            // TmdbDetails의 credits에 가공한 데이터 세팅
            tmdbDetails.getCredits().setCast(castList);
            tmdbDetails.getCredits().setCrew(crewList);

            if (type.equals(ContentsType.TV)) {

                // TV 데이터 DB 저장 및 contetns_id 설정
                log.info("TV 데이터 DB 저장 시작 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getName());
                savedTvProgramData = contentsService.saveTvData(
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
                log.info("TV 데이터 DB 저장 완료 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getName());

                // 시즌 데이터 DB 저장
                log.info("SEASON 정보 DB 저장 시작");

                List<DetailsSeason> seasons = tmdbDetails.getSeasons();
                for (DetailsSeason s : seasons) {

                    Season savedSeason = seasonService.saveSeasonInfo(Season.toEntity(s));
                    seasonService.saveSeasonCollection(savedSeason, savedTvProgramData);
                }
                log.info("SEASON 정보 DB 저장 완료");

                // 방송사 데이터 DB 저장
                log.info("방송자 정보 DB 저장 시작");
                List<DetailsNetworks> networks = tmdbDetails.getNetworks();
                for (DetailsNetworks n : networks) {
                    Networks savedNetworksInfo = networksService.saveNetworksInfo(Networks.toEntity(n));
                    networksService.saveBroadcaster(savedNetworksInfo, savedTvProgramData);
                }
                log.info("방송자 정보 DB 저장 완료");

            } else if (type.equals(ContentsType.MOVIE)) {

                // MOVIE 데이터 DB 저장 및 contetns_id 설정
                log.info("MOVIE 데이터 DB 저장 시작 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getTitle());
                savedMovieData = contentsService.saveMovieData(
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
                );
                tmdbDetails.setContentsId(savedMovieData.getContentsId());
                log.info("MOVIE 데이터 DB 저장 완료 : [" + tmdbDetails.getTmdbId() + "] " + tmdbDetails.getTitle());
            }

            // Credits 데이터 DB 저장
            // castList와 crewList 데이터를 합쳐서 하나의 리스트에서 루프
            log.info("Credits 정보 DB 저장 시작");
            for (DetailsPerson person : Stream.concat(castList.stream(), crewList.stream()).toList()) {
                Person savedPersonEntity = personService.savePersonEntity(Person.toEntity(person));
                if (type.equals(ContentsType.TV)) {
                    personService.saveCredit(person, savedPersonEntity, savedTvProgramData);
                } else if (type.equals(ContentsType.MOVIE)) {
                    personService.saveCredit(person, savedPersonEntity, savedMovieData);
                }
            }
            log.info("Credits 정보 DB 저장 완료");

            log.info("TMDB API details 및 credits 정보 DB 저장 완료");
            return tmdbDetails;

        } catch (Exception e) {
            log.error("에러가 발생하여 정상적으로 작품 DB 저장을 완료하지 못했습니다. 이전의 상태로 롤백합니다.");
            throw new RuntimeException(e);
        }
    }


    private ContentsType checkAnimationGenre(Set<Integer> genres, ContentsType type) {
        if (genres.contains(16)) {
            return ContentsType.ANIMATION;
        } else {
            return type.equals(ContentsType.TV) ? ContentsType.TV : ContentsType.MOVIE;
        }
    }

    private List<DetailsPerson> getCastListWithMaximumSize(List<DetailsPerson> originalCastList) {
        // Cast(배우)의 경우, 최대 N명 분의 데이터만 저장한다.
        originalCastList.forEach(person -> person.setCreditType(CreditType.CAST));
        return originalCastList.subList(0, Math.min(originalCastList.size(), MAX_CAST_LIST_SIZE));
    }

    private List<DetailsPerson> mergeAndCleanDuplicateCrew(List<DetailsPerson> originalCrewList) {
        Map<Long, DetailsPerson> crewDuplicationMap = new HashMap<>();

        // Crew(감독, 작가, PD 등)의 경우, 중복되는 인물이라면 knowForDepartment, department, job을 컴마로 구분하여 한 객체에 저장한다
        for (DetailsPerson person : originalCrewList) {
            if (isValidCrewJob(person.getJob())) {
                long id = person.getId();

                // id로 중복검사 시행
                if (crewDuplicationMap.containsKey(id)) {
                    DetailsPerson existingCrew = crewDuplicationMap.get(id);
                    existingCrew.setKnownForDepartment(existingCrew.getKnownForDepartment() + "," + person.getKnownForDepartment());
                    existingCrew.setDepartment(existingCrew.getDepartment() + "," + person.getDepartment());
                    existingCrew.setJob(existingCrew.getJob() + "," + person.getJob());

                    // 인물 중요도 +1
                    existingCrew.setImportanceOrder(existingCrew.getImportanceOrder() + 1);
                } else {
                    person.setCreditType(CreditType.CREW);
                    crewDuplicationMap.put(id, person);
                }
            }
        }

        // 통합한 문자열에 중복되는 데이터가 있다면 중복제거
        List<DetailsPerson> crewList = new ArrayList<>(crewDuplicationMap.values());
        for (DetailsPerson crew : crewList) {
            crew.setKnownForDepartment(removeDuplicatesAndJoin(crew.getKnownForDepartment()));
            crew.setDepartment(removeDuplicatesAndJoin(crew.getDepartment()));
            crew.setJob(removeDuplicatesAndJoin(crew.getJob()));
        }

        return crewList;
    }

    private boolean isValidCrewJob(String job) {
        return job.equals("Producer") || job.equals("Executive Producer")  || job.equals("Director")
                || job.equals("Writer") || job.equals("Story") || job.equals("Screenplay");
    }

    private String removeDuplicatesAndJoin(String input) {
        String[] items = input.split(",");
        Set<String> uniqueItems = new LinkedHashSet<>(Arrays.asList(items));  // 중복 제거하면서 순서 유지

        return String.join(",", uniqueItems);
    }
}
