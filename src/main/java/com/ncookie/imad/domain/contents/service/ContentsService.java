package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.global.openfeign.TmdbFeignClient;
import feign.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbFeignClient feignClient;

    String apiKey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4ODM2NWNmNmJkYzZjNDYwMTViZjg1YzczMWRmYWI4ZiIsInN1YiI6IjY0NTRlZmFhZDQ4Y2VlMDEzNmRhMWM1MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.QZC-wgg4ipi9UgxmLjrTzUtrW6C8S5u_pINevgwr97k";


    public void searchKeywords() {
        Response response = feignClient.searchByKeywords(
                apiKey,
                MediaType.APPLICATION_JSON_VALUE,
                "breaking",
                true,
                "ko-kr",
                1
        );
        System.out.println(response.status());
        System.out.println(response.body());
    }
}
