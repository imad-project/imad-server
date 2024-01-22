package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("MONTHLY")
public class RankingMonthly extends RankingBaseEntity {

}
