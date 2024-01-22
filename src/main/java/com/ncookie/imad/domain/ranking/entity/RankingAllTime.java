package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("ALL_TIME")
@SuperBuilder
@AllArgsConstructor
public class RankingAllTime extends RankingBaseEntity {

}
