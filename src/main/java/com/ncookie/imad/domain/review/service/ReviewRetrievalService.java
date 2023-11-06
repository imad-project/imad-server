package com.ncookie.imad.domain.review.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review.repository.ReviewRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Description("Review 조회 용도로만 사용하는 서비스 클래스")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReviewRetrievalService {
    private final ReviewRepository reviewRepository;

    public Review findByContentsAndUser(Contents contents, UserAccount user) {
        return reviewRepository.findByContentsAndUserAccount(contents, user);
    }
}
