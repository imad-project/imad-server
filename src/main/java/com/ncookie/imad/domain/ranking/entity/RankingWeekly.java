package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("WEEKLY")
public class RankingWeekly extends RankingBaseEntity {

}
