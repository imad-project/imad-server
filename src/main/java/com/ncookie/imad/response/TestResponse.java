package com.ncookie.imad.response;

import com.ncookie.imad.dto.TestDto;

import java.io.Serializable;

public record TestResponse(Long id) implements Serializable {
    public static TestResponse of(Long id) {
        return new TestResponse(id);
    }

    public static TestResponse from(TestDto dto) {
        return new TestResponse(dto.id());
    }
}
