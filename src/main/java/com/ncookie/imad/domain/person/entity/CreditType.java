package com.ncookie.imad.domain.person.entity;

import lombok.Getter;

@Getter
public enum CreditType {
    CAST("CAST"),
    CREW("CREW");

    private final String creditType;

    CreditType(String creditType) {
        this.creditType = creditType;
    }
}
