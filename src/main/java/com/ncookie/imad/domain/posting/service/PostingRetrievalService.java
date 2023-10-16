package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.PostingRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class PostingRetrievalService {
    private final PostingRepository postingRepository;


    public Posting getPostingEntityById(Long postingId) {
        Optional<Posting> optionalPosting = postingRepository.findById(postingId);

        if (optionalPosting.isPresent()) {
            return optionalPosting.get();
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
    }
}
