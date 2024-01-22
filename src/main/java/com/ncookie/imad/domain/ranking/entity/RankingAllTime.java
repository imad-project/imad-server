package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("ALL_TIME")
public class RankingAllTime extends RankingBaseEntity {

}
