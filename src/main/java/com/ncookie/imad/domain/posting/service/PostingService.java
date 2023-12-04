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
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.profile.service.ScrapService;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private final UserRetrievalService userRetrievalService;
    private final ContentsService contentsService;

    private final PostingLikeService postingLikeService;
    private final CommentService commentService;
    private final ScrapService scrapService;


    public PostingDetailsResponse getPosting(String accessToken, Long postingId) {
        Posting posting = getPostingEntityById(postingId);
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 댓글 조회
        // 게시글 조회 시 댓글 조회는 항상 날짜 기준/오름차순으로 첫 페이지의 데이터만 조회함
        CommentListResponse commentList = commentService.getCommentListByPosting(
                accessToken,
                postingId,
                0,
                null,
                0,
                "createdDate",
                0);
        int commentCount = commentService.getCommentCount(posting);

        // like status 조회
        PostingLike postingLike = postingLikeService.findByUserAccountAndE(user, posting);
        int likeStatus = postingLike == null ? 0 : postingLike.getLikeStatus();
        
        // 스크랩 여부 조회
        PostingScrap scrap = scrapService.findByUserAccountAndContents(user, posting);

        // TODO: 특정 기준(accessToken에 조회 여부 저장 등)을 통해 중복 조회수를 필터링 해야함
        // 조회수 갱신
        postingRepository.updateViewCount(postingId, posting.getViewCnt() + 1);

        // 게시글 정보, 댓글 리스트, like status 등을 DTO 객체에 저장
        PostingDetailsResponse postingDetailsResponse = PostingDetailsResponse.toDTO(posting, commentList);
        postingDetailsResponse.setLikeStatus(likeStatus);
        postingDetailsResponse.setCommentCnt(commentCount);
        postingDetailsResponse.setScrapId(scrap != null ? scrap.getId() : null);
        postingDetailsResponse.setScrapStatus(scrap != null);

        return postingDetailsResponse;
    }

    public PostingListResponse getAllPostingList(String accessToken, int pageNumber, int category) {
        Page<Posting> postingPage;
        if (category == 0) {
            postingPage = postingRepository.findAll(Utils.getDefaultPageable(pageNumber));
        } else {
            postingPage = postingRepository.findAllByCategory(Utils.getDefaultPageable(pageNumber), category);
        }

        return getPostingListResponseByPage(
                accessToken,
                postingPage,
                null
        );
    }

    public PostingListResponse getAllPostingListByQuery(String accessToken,
                                                        int searchType,
                                                        String query,
                                                        int pageNumber,
                                                        String sortString,
                                                        int order,
                                                        int category) {
        PageRequest pageable = Utils.getPageRequest(pageNumber, sortString, order);

        // 검색 타입에 따라 repository에 데이터 요청
        Page<Posting> postingPage;
        if (category == 0) {
            postingPage = switch (searchType) {
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
        } else {
            postingPage = switch (searchType) {
                // 제목 + 본문 + 카테고리
                case 0 -> postingRepository.findAllByCategoryAndTitleContainingOrCategoryAndContentContaining(
                        pageable,
                        category,
                        query,
                        category,
                        query);

                // 제목 + 카테고리
                case 1 -> postingRepository.findAllByTitleContainingAndCategory(pageable, query, category);

                // 본문 + 카테고리
                case 2 -> postingRepository.findAllByContentContainingAndCategory(pageable, query, category);

                // 작성자 + 카테고리
                case 3 -> postingRepository.findAllByCategoryAndUserNicknameContaining(pageable, category, query);

                default -> throw new BadRequestException(ResponseCode.POSTING_WRONG_SEARCH_TYPE);
            };
        }

        return getPostingListResponseByPage(accessToken, postingPage, searchType);
    }

    private PostingListResponse getPostingListResponseByPage(String accessToken, Page<Posting> postingPage, Integer searchType) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return PostingListResponse.toDTO(
                postingPage,
                convertPostingListToPostingDetailsResponseList(user, postingPage.stream().toList()),
                searchType
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

            // 스크랩 여부 조회
            PostingScrap scrap = scrapService.findByUserAccountAndContents(user, posting);

            // DTO 클래스 변환
            PostingListElement postingDetailsResponse = PostingListElement.toDTO(posting);
            
            // like status 설정
            postingDetailsResponse.setLikeStatus(likeStatus);
            
            // 댓글 수 설정
            postingDetailsResponse.setCommentCnt(commentCount);

            // 스크랩 설정
            postingDetailsResponse.setScrapId(scrap != null ? scrap.getId() : null);
            postingDetailsResponse.setScrapStatus(scrap != null);
            
            postingDetailsResponseList.add(postingDetailsResponse);
        }

        return postingDetailsResponseList;
    }

    public PostingIdResponse addPosting(String accessToken, AddPostingRequest addPostingRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

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
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

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

    public PostingListResponse getPostingListByUser(UserAccount user, int pageNumber) {
        Page<Posting> postingPage = postingRepository.findAllByUser(user, Utils.getDefaultPageable(pageNumber));

        return PostingListResponse.toDTO(
                postingPage,
                convertPostingListToPostingDetailsResponseList(
                        user,
                        postingPage.getContent().stream().toList()),
                null);

    }

    public PostingListResponse getLikedPostingListByUser(UserAccount user, int pageNumber, int likeStatus) {
        Page<PostingLike> postingLikePage = postingLikeService.getLikedListByUser(
                user,
                Utils.getDefaultPageable(pageNumber),
                likeStatus);

        List<Posting> postingList = new ArrayList<>();
        for (PostingLike postingLike : postingLikePage.getContent().stream().toList()) {
            postingList.add(postingLike.getPosting());
        }

        return PostingListResponse.toDTO(
                postingLikePage,
                convertPostingListToPostingDetailsResponseList(user, postingList),
                null
        );
    }
}
