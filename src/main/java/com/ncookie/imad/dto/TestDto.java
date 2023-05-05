package com.ncookie.imad.dto;

public record TestDto(Long id) {
    public static TestDto of(Long id) {
        return new TestDto(id);
    }
}
