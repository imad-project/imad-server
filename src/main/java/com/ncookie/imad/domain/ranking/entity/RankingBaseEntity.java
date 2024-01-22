package com.ncookie.imad.domain.ranking.entity;


import com.ncookie.imad.domain.contents.entity.Contents;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONTENTS_TYPE")
@Table(indexes = {
        @Index(columnList = "rank")
})
public class RankingBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private Contents contents;

    // 어제자 데이터와의 랭킹 차이. 어제자 랭킹에 없는 작품인 경우 NULL값이 들어감
    private Long rankChanged;

    // 현재 랭킹
    private Long rank;
}
