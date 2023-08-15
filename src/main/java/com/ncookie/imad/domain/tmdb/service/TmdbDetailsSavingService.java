package com.ncookie.imad.domain.tmdb.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.tmdb.dto.DetailsGenre;
import com.ncookie.imad.domain.tmdb.dto.DetailsMovie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TmdbDetailsSavingService {
    public final ContentsService contentsService;

    @Transactional
    public void saveContentsDetails(String detailsJsonData, String type) {
        // TODO: Contents(MovieData, TvProgramData), Networks, Season, Person Entity 저장
        /*
         * JSON 데이터를 분리해야 함
         * builder로 각 DTO 생성해주고, 해당하는 도메인의 service에 값을 전달하여 DB에 저장하도록 해야함
         * 이를 위해 도메인 별 서비스에 repository save 메소드 추가
         *
         * DTO 클래스를 구현한 builder 객체를 연결 테이블에 할당하여 관계 구축
         */

        ObjectMapper objectMapper = new ObjectMapper();

        try {
//            Set<DetailsGenre> genres = objectMapper.readValue(detailsJsonData.get("genres").toString(), new TypeReference<Set<DetailsGenre>>() {});
            DetailsMovie detailsMovie = objectMapper.readValue(detailsJsonData, DetailsMovie.class);
            System.out.println(detailsMovie);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<Integer> genreList;
        // 장르 테이블이 별도로 존재할 필요가 있는가?
//        for (DetailsGenre genre : genres) {
//
//        }

        if (type.equals("tv")) {
//            Contents.builder()
//                    .tmdbId((Long) detailsJsonData.get("id"))
//                    .build();
        } else if (type.equals("movie")) {

        }

        System.out.println("hi");
    }
}
