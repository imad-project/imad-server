package com.ncookie.imad.domain.ranking.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RankingPeriod {
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    ALL_TIME("alltime");

    private final String value;
}
