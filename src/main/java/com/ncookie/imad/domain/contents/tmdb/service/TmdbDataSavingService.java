package com.ncookie.imad.domain.contents.tmdb.service;

import com.ncookie.imad.domain.contents.service.ContentsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class TmdbDataSavingService {
    public final ContentsService contentsService;

    public void save() {
        // TODO: 기존에 해당 데이터가 있는지 확인 후, 있다면 업데이트(덮어쓰기) 진행
        // TODO: Contents(MovieData, TvProgramData), Networks, Season, Person Entity 저장

    }
}
