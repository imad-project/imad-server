package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Table(indexes = {
        @Index(columnList = "contents_id"),
        @Index(columnList = "contents_type")
})
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Entity
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contents_id")
    private Long contentsId;

    @Setter private Long tmdbId;
    @Setter private ContentsType tmdbType;

    @Enumerated(EnumType.STRING)
    @Column(name = "contents_type", nullable = false)
    // MOVIE, TV, ANIMATION으로 구성되어 있음
    // TV와 ANIMATION은 사실상 같은 구조를 가지고 있지만 애니메이션을 따로 분류하기 위해 장르의 `animation`으로 구분함
    private ContentsType contentsType;

    @Setter private String translatedTitle;
    @Setter private String originalTitle;
    @Setter private String originalLanguage;

    @Column(length = 1000)
    @Setter
    private String overview;
    @Setter
    private String tagline;
    @Setter
    private String posterPath;
    @Setter
    private String status;

    // 여러 개의 데이터를 넣기 위해서 @ElementCollection 어노테이션 생성
    // JPA에서 자동으로 countries 테이블을 만들어줌
    @ElementCollection
    @CollectionTable(name = "countries", joinColumns = @JoinColumn(name = "contents_id", referencedColumnName = "contents_id"))
    @Builder.Default
    private Set<String> productionCountries = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "contents_genre", joinColumns = @JoinColumn(name = "contents_id", referencedColumnName = "contents_id"))
    @Builder.Default
    private Set<Integer> contentsGenres = new HashSet<>();

    @Setter
    // 시청 등급
    private String certification;

    @Setter private int reviewCnt;
    @Setter private int postingCnt;
    @Setter private float imadScore;
}
