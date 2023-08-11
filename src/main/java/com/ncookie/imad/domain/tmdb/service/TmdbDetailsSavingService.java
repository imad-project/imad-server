package com.ncookie.imad.domain.tmdb.service;

import com.ncookie.imad.domain.contents.service.ContentsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TmdbDetailsSavingService {
    public final ContentsService contentsService;

    @Transactional
    public void saveContentsDetails(Map<String, Object> detailsData, String type) {
        // TODO: Contents(MovieData, TvProgramData), Networks, Season, Person Entity 저장
        /*
         * JSON 데이터를 분리해야 함
         * builder로 각 DTO 생성해주고, 해당하는 도메인의 service에 값을 전달하여 DB에 저장하도록 해야함
         * 이를 위해 도메인 별 서비스에 repository save 메소드 추가
         *
         * DTO 클래스를 구현한 builder 객체를 연결 테이블에 할당하여 관계 구축
         */

        if (type.equals("tv")) {

        } else if (type.equals("movie")) {

        }
    }
}
