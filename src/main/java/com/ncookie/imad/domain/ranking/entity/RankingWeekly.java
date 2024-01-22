package com.ncookie.imad.domain.ranking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("WEEKLY")
@SuperBuilder
@AllArgsConstructor
public class RankingWeekly extends RankingBaseEntity {

}
