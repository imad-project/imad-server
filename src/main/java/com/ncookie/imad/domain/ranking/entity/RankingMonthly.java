package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("MONTHLY")
@SuperBuilder
@AllArgsConstructor
public class RankingMonthly extends RankingBaseEntity {

}
