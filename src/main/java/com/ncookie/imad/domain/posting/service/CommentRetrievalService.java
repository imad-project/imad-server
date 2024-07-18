package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.repository.CommentRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Description("Comment entity 조회 용도로만 사용하는 서비스 클래스")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentRetrievalService {
    private final CommentRepository commentRepository;

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
}
