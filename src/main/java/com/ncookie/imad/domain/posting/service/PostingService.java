package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.like.entity.PostingLike;
import com.ncookie.imad.domain.like.service.PostingLikeService;
import com.ncookie.imad.domain.posting.dto.request.AddPostingRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyPostingRequest;
import com.ncookie.imad.domain.posting.dto.response.*;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.PostingRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostingService {
    private final PostingRepository postingRepository;

    private final UserAccountService userAccountService;
    private final ContentsService contentsService;

    private final PostingLikeService postingLikeService;
    private final CommentService commentService;

    private final int PAGE_SIZE = 10;


    public PostingDetailsResponse getPosting(String accessToken, Long postingId) {
        Posting posting = getPostingEntityById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        // 댓글 조회
        // 게시글 조회 시 댓글 조회는 항상 날짜 기준/오름차순으로 첫 페이지의 데이터만 조회함
        CommentListResponse commentList = commentService.getCommentListByPosting(
                accessToken,
                posting,
                0,
                "createdDate",
                0);

        // like status 조회
        PostingLike postingLike = postingLikeService.findByUserAccountAndE(user, posting);
        int likeStatus = postingLike == null ? 0 : postingLike.getLikeStatus();

        // TODO: 특정 기준(accessToken에 조회 여부 저장 등)을 통해 중복 조회수를 필터링 해야함
        // 조회수 갱신
        postingRepository.updateViewCount(postingId, posting.getViewCnt() + 1);

        // 게시글 정보, 댓글 리스트, like status 등을 DTO 객체에 저장
        PostingDetailsResponse postingDetailsResponse = PostingDetailsResponse.toDTO(posting, commentList);
        postingDetailsResponse.setLikeStatus(likeStatus);

        return postingDetailsResponse;
    }

    public PostingListResponse getAllPostingList(String accessToken, int pageNumber) {
        Sort sort = Sort.by("createdDate").descending();
        PageRequest pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);

        return getPostingListResponseByPage(accessToken, postingRepository.findAll(pageable));
    }

    public PostingListResponse getAllPostingListByQuery(String accessToken,
                                             int searchType,
                                             String query,
                                             int pageNumber,
                                             String sortString,
                                             int order) {

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
        } catch (PropertyReferenceException e) {
            // sort string에 잘못된 값이 들어왔을 때 에러 발생
            throw new BadRequestException(ResponseCode.POSTING_WRONG_SORT_STRING);
        }

        // 검색 타입에 따라 repository에 데이터 요청
        Page<Posting> postingPage = switch (searchType) {
            // 제목 + 본문
            case 0 -> postingRepository.findAllByTitleContainingOrContentContaining(pageable, query, query);
            
            // 제목
            case 1 -> postingRepository.findAllByTitleContaining(pageable, query);
            
            // 본문
            case 2 -> postingRepository.findAllByContentContaining(pageable, query);
            
            // 작성자
            case 3 -> postingRepository.findAllByUserNicknameContaining(pageable, query);

            default -> throw new BadRequestException(ResponseCode.POSTING_WRONG_SEARCH_TYPE);
        };

        return getPostingListResponseByPage(accessToken, postingPage);
    }

    private PostingListResponse getPostingListResponseByPage(String accessToken, Page<Posting> postingPage) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        return PostingListResponse.toDTO(
                postingPage,
                convertPostingListToPostingDetailsResponseList(user, postingPage.stream().toList())
        );
    }
    
    private List<PostingListElement> convertPostingListToPostingDetailsResponseList(UserAccount user, List<Posting> postingList) {
        // Posting 클래스를 PostingDetailsResponse 데이터 형식에 맞게 매핑
        List<PostingListElement> postingDetailsResponseList = new ArrayList<>();
        for (Posting posting : postingList) {
            // 좋아요/싫어요 여부 데이터
            PostingLike postingLike = postingLikeService.findByUserAccountAndE(user, posting);
            int likeStatus = postingLike == null ? 0 : postingLike.getLikeStatus();

            // 댓글수 데이터
            int commentCount = commentService.getCommentCount(posting);

            // DTO 클래스 변환 및 like status 설정
            PostingListElement postingDetailsResponse = PostingListElement.toDTO(posting);
            postingDetailsResponse.setLikeStatus(likeStatus);
            postingDetailsResponse.setCommentCnt(commentCount);
            postingDetailsResponseList.add(postingDetailsResponse);
        }

        return postingDetailsResponseList;
    }

    public PostingIdResponse addPosting(String accessToken, AddPostingRequest addPostingRequest) {
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

        return PostingIdResponse.builder()
                .postingId(posting.getPostingId())
                .build();
    }

    public PostingIdResponse modifyPosting(String accessToken, Long postingId, ModifyPostingRequest modifyPostingRequest) {
        Posting posting = getPostingEntityById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (posting.getUser().getId().equals(user.getId())) {
            posting.setTitle(modifyPostingRequest.getTitle());
            posting.setContent(modifyPostingRequest.getContent());
            posting.setCategory(modifyPostingRequest.getCategory());
            posting.setSpoiler(modifyPostingRequest.isSpoiler());

            return PostingIdResponse.builder()
                    .postingId(postingRepository.save(posting).getPostingId())
                    .build();
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NO_PERMISSION);
        }
    }

    public void deletePosting(String accessToken, Long postingId) {
        Posting posting = getPostingEntityById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        if (posting.getUser().getId().equals(user.getId())) {
            postingRepository.delete(posting);
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NO_PERMISSION);
        }
    }

    public void saveLikeStatus(String accessToken, Long postingId, int likeStatus) {
        if (likeStatus != -1 && likeStatus != 0 && likeStatus != 1) {
            throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
        }

        Posting posting = getPostingEntityById(postingId);
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);

        PostingLike postingLike = postingLikeService.findByUserAccountAndE(user, posting);

        // postingLike 신규등록
        if (postingLike == null) {
            postingLikeService.saveLikeStatus(PostingLike.builder()
                    .userAccount(user)
                    .posting(posting)
                    .likeStatus(likeStatus)
                    .build());
        } else {
            // like_status가 1이면 좋아요, -1이면 싫어요, 0이면 둘 중 하나를 취소한 상태이므로 테이블에서 데이터 삭제
            if (likeStatus == 0) {
                postingLikeService.deleteLikeStatus(postingLike);
            } else {
                postingLike.setLikeStatus(likeStatus);
                PostingLike savedPostingLikeStatus = postingLikeService.saveLikeStatus(postingLike);

                // postingLike entity 저장/수정 실패
                if (savedPostingLikeStatus == null) {
                    throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
                }
            }
        }

        // like, dislike count 갱신
        postingRepository.updateLikeCount(posting.getPostingId(), postingLikeService.getLikeCount(posting));
        postingRepository.updateDislikeCount(posting.getPostingId(), postingLikeService.getDislikeCount(posting));
    }
    
    private Posting getPostingEntityById(Long id) {
        Optional<Posting> postingOptional = postingRepository.findById(id);

        if (postingOptional.isPresent()) {
            log.info("게시글 entity 조회 완료");
            return postingOptional.get();
        } else {
            log.info("게시글 entity 조회 실패 : 해당 ID의 댓글을 찾을 수 없음");
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
    }
}
