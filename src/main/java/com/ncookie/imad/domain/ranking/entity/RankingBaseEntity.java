package com.ncookie.imad.domain.ranking.entity;


import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class RankingBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private Contents contents;

    // ALL, MOVIE, TV, ANIMATION 등이 있음
    private ContentsType contentsType;

    // 랭킹 순위
    private Long ranking;           // 전체 작품 중 랭킹 순위
    private Long rankingTv;         // TV 작품 중 랭킹 순위
    private Long rankingMovie;      // MOVIE 작품 중 랭킹 순위
    private Long rankingAnimation;  // ANIMATION 작품 중 랭킹 순위


    // 어제자 데이터와의 랭킹 차이. 어제자 랭킹에 없는 작품인 경우 NULL값이 들어감
    private Long rankingChanged;
    private Long rankingChangedTv;
    private Long rankingChangedMovie;
    private Long rankingChangedAnimation;

    // 랭킹 점수
    private Long rankingScore;
}
