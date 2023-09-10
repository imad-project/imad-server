package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.networks.dto.DetailsNetworks;
import com.ncookie.imad.domain.person.dto.DetailsCredits;
import com.ncookie.imad.domain.season.dto.DetailsSeason;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// 작품 상세 정보를 클라이언트에게 전달해주기 위한 DTO 클래스
public class TmdbDetails {
    private long contentsId;                        // IMAD 자체적으로 사용하는 id
    private long tmdbId;                            // TMDB 내부적으로 사용하는 id

    private String overview;                        // 작품 개요
    private String tagline;                         // 작품의 핵심이 되는 포인트나 제목에 대한 부연설명
    private String posterPath;                      // 포스터
    private String originalLanguage;                // 원어
    private String certification;                   // 영상물 등급

    private String status;                          // 제작 중, 개봉함, 방영함 등의 값을 가짐

    private Set<Integer> genres;                    // 장르 리스트
    private Set<String> productionCountries;        // 제작 국가


    // IMAD Data
    private ContentsType contentsType;
    private int reviewCnt;                          // 리뷰 개수
    private float imadScore;                        // IMAD 평점 평균


    // Movie Data
    private String title;
    private String originalTitle;

    private String releaseDate;                      // 개봉일
    private int runtime;                             // 상영시간


    // TV Data
    private String name;
    private String originalName;

    private String firstAirDate;                      // 첫화 방영일
    private String lastAirDate;                       // 마지막화 방영일

    private int numberOfEpisodes;                     // 에피소드 개수
    private int numberOfSeasons;                      // 시즌 개수

    private List<DetailsSeason> seasons;              // 시즌 정보
    private List<DetailsNetworks> networks;           // 작품 방영한 방송사 정보


    // Credits
    @JsonProperty("credits")
    private DetailsCredits credits;                    // 출연진(배우, 감독, 작가, 스태프 등 포함) 정보


    @JsonCreator
    public TmdbDetails(
            @JsonProperty("id") int id,
            @JsonProperty("genres") Set<DetailsGenre> genreSet,
            @JsonProperty("production_countries") Set<ProductionCountry> productionCountrySet) {
        this.tmdbId = id;

        this.genres = new HashSet<>();
        genreSet.forEach(genre -> this.genres.add(genre.getId()));

        this.productionCountries = new HashSet<>();
        productionCountrySet.forEach(country -> this.productionCountries.add(country.getIso_3166_1()));
    }
}
