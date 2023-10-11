package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.posting.dto.AddPostingRequest;
import com.ncookie.imad.domain.posting.dto.AddPostingResponse;
import com.ncookie.imad.domain.posting.dto.ModifyPostingRequest;
import com.ncookie.imad.domain.posting.dto.PostingDetailsResponse;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.PostingRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostingService {
    private final PostingRepository postingRepository;

    private final UserAccountService userAccountService;
    private final ContentsService contentsService;


    public AddPostingResponse addPosting(String accessToken, AddPostingRequest addPostingRequest) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(addPostingRequest.getContentsId());

        Posting posting = postingRepository.save(
                Posting.builder()
                        .user(user)
                        .contents(contents)

                        .title(addPostingRequest.getTitle())
                        .content(addPostingRequest.getContent())
                        .category(addPostingRequest.getCategory())

                        .isSpoiler(addPostingRequest.isSpoiler())

                        .build()
        );

        return AddPostingResponse.builder()
                .postingId(posting.getPostingId())
                .build();
    }

    public PostingDetailsResponse getPosting(Long postingId) {
        Optional<Posting> optional = postingRepository.findById(postingId);

        if (optional.isPresent()) {
            Posting posting = optional.get();

            return PostingDetailsResponse.toDTO(posting);
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
    }

    public Long modifyPosting(String accessToken, Long postingId, ModifyPostingRequest modifyPostingRequest) {
        Optional<Posting> optional = postingRepository.findById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Posting posting = optional.get();

            if (posting.getUser().getId().equals(user.getId())) {
                posting.setTitle(modifyPostingRequest.getTitle());
                posting.setContent(modifyPostingRequest.getContent());
                posting.setCategory(modifyPostingRequest.getCategory());
                posting.setSpoiler(modifyPostingRequest.isSpoiler());

                return postingRepository.save(posting).getPostingId();
            } else {
                throw new BadRequestException(ResponseCode.POSTING_MODIFY_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }

    }
}
