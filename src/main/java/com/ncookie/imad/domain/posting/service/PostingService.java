package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.posting.dto.AddPostingRequest;
import com.ncookie.imad.domain.posting.dto.ModifyPostingRequest;
import com.ncookie.imad.domain.posting.dto.PostingDetailsResponse;
import com.ncookie.imad.domain.posting.dto.PostingListResponse;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.PostingRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostingService {
    private final PostingRepository postingRepository;

    private final UserAccountService userAccountService;
    private final ContentsService contentsService;


    public PostingDetailsResponse getPosting(Long postingId) {
        Optional<Posting> optional = postingRepository.findById(postingId);

        if (optional.isPresent()) {
            Posting posting = optional.get();

            return PostingDetailsResponse.toDTO(posting);
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
    }

    public PostingListResponse getPostingList(String accessToken,
                                              int searchType,
                                              int pageNumber,
                                              String sortString,
                                              int order) {
        final int PAGE_SIZE = 10;

        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        // sort가 null이거나, sort 설정 중 에러가 발생했을 때의 예외처리도 해주어야 함
        Sort sort;
        PageRequest pageable;
        try {
            if (order == 0) {
                // 오름차순 (ascending)
                sort = Sort.by(sortString).ascending();
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
            } else if (order == 1) {
                // 내림차순 (descending)
                sort = Sort.by(sortString).descending();
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
            } else {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            }

//            Page<Posting> postingPage = postingRepository.findAllById(contents, pageable);
            return PostingListResponse.toDTO(
                    null,
                    null
            );
        } catch (PropertyReferenceException e) {
            // sort string에 잘못된 값이 들어왔을 때 에러 발생
            throw new BadRequestException(ResponseCode.REVIEW_GET_LIST_SORT_STRING_WRONG);
        }
    }

    public Long addPosting(String accessToken, AddPostingRequest addPostingRequest) {
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

        return posting.getPostingId();
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

    public void deletePosting(String accessToken, Long postingId) {
        Optional<Posting> optional = postingRepository.findById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (optional.isPresent()) {
            Posting posting = optional.get();

            if (posting.getUser().getId().equals(user.getId())) {
                postingRepository.delete(posting);
            } else {
                throw new BadRequestException(ResponseCode.POSTING_MODIFY_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
    }
}
