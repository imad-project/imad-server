package com.ncookie.imad;

import com.ncookie.imad.feigntest.FeignTest;
import com.ncookie.imad.global.openfeign.TmdbFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
@RequiredArgsConstructor
@FeignTest
public class TmdbOpenFeignTest {
    private TmdbFeignClient feignClient;

    @Test
    void OpenFeign테스트() {
        // Given

        // When

        // Then
    }
}
