package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.repository.ContentsRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Description("Contents entity 조회 용도로만 사용하는 서비스 클래스")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ContentsRetrievalService {
    private final ContentsRepository contentsRepository;

    public Contents getContentsById(Long contentsId) {
        Optional<Contents> optionalContents = contentsRepository.findById(contentsId);

        if (optionalContents.isPresent()) {
            log.info("작품 정보 반환 성공");
            return optionalContents.get();
        } else {
            log.warn("ID에 해당하는 작품이 존재하지 않음");
            return null;
        }
    }
}
