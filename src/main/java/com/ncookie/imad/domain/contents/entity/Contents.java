package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(indexes = {
        @Index(columnList = "contents_id"),
        @Index(columnList = "contents_type")
})
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tmdb_type")
@Entity
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contents_id")
    private Long id;

    @Setter private int tmdb_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contents_type", nullable = false)
    private ContentsType contentsType;

    @Setter private String translatedTitle;
    @Setter private String originalTitle;
    @Setter private String originalLanguage;
    @Setter private String tagLine;

    @Column(length = 1000)
    @Setter private String overview;
    @Setter private String posterPath;
    @Setter private String production_countries;

    // 여러 개의 데이터를 넣기 위해서 @ElementCollection 어노테이션 생성
    // JPA에서 자동으로 countries 테이블을 만들어줌
    @ElementCollection
    @CollectionTable(name = "countries")
    @Builder.Default
    private List<String> productionCountries = new ArrayList<>();

    @Setter private String certification;

    @Setter private int reviewCnt;
    @Setter private int articleCnt;
    @Setter private float imadScore;
}
