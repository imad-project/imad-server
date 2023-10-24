package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.like.entity.CommentLike;
import com.ncookie.imad.domain.like.service.CommentLikeService;
import com.ncookie.imad.domain.posting.dto.request.AddCommentRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyCommentRequest;
import com.ncookie.imad.domain.posting.dto.response.CommentDetailsResponse;
import com.ncookie.imad.domain.posting.dto.response.CommentIdResponse;
import com.ncookie.imad.domain.posting.dto.response.CommentListResponse;
import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.CommentRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import jdk.jfr.Description;
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
public class CommentService {
    private final CommentRepository commentRepository;

    private final CommentLikeService commentLikeService;

    private final UserRetrievalService userRetrievalService;
    private final PostingRetrievalService postingRetrievalService;


    public CommentDetailsResponse getComment(String accessToken, Long commentId) {
        Comment comment = getCommentEntityById(commentId);
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        
        return getCommentDetailsResponse(user, comment);
    }

    @Description("Comment entity 사용하여 CommentDetailsResponse로 변환하여 반환해주는 메소드")
    private CommentDetailsResponse getCommentDetailsResponse(UserAccount user, Comment comment) {

        // like status 조회
        CommentLike commentLike = commentLikeService.findByUserAccountAndE(user, comment);
        int likeStatus = commentLike == null ? 0 : commentLike.getLikeStatus();

        return CommentDetailsResponse.toDTO(comment, likeStatus);
    }

    public CommentListResponse getCommentListByPosting(String accessToken,
                                                       Long postingId,
                                                       int commentType,
                                                       Long parentId,
                                                       int pageNumber,
                                                       String sortString,
                                                       int order) {
        PageRequest pageable = Utils.getPageRequest(pageNumber, sortString, order);

        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Posting posting = postingRetrievalService.getPostingEntityById(postingId);

        Page<Comment> commentPage;
        if (commentType == 0) {
            // 댓글
            commentPage = commentRepository.findAllByPostingAndParentNull(posting, pageable);
        } else if (commentType == 1) {
            // 답글
            Comment parentComment = getCommentEntityById(parentId);
            commentPage = commentRepository.findAllByPostingAndParent(posting, parentComment, pageable);
        } else {
            throw new BadRequestException(ResponseCode.COMMENT_LIST_WRONG_TYPE);
        }

        List<CommentDetailsResponse> commentDetailsResponseList = new ArrayList<>();
        for (Comment c : commentPage.getContent().stream().toList()) {
            commentDetailsResponseList.add(getCommentDetailsResponse(user, c));
        }

        return CommentListResponse.toDTO(commentPage, commentDetailsResponseList);
    }

    public CommentIdResponse addComment(String accessToken, AddCommentRequest addCommentRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Posting posting = postingRetrievalService.getPostingEntityById(addCommentRequest.getPostingId());

        Comment comment = Comment.builder()
                .posting(posting)
                .userAccount(user)

                .content(addCommentRequest.getContent())
                .isRemoved(false)

                .build();

        if (addCommentRequest.getParentId() != null) {
            // 자식 댓글(답글) 등록
            Comment parentComment = getCommentEntityById(addCommentRequest.getParentId());
            comment.setParent(parentComment);

            log.info("답글의 부모 댓글 설정 완료");
        }

        Comment save = commentRepository.save(comment);
        log.info("댓글(답글) 등록 완료");

        return CommentIdResponse.builder()
                .commentId(save.getCommentId())
                .build();
    }

    public CommentIdResponse modifyComment(String accessToken, Long commentId, ModifyCommentRequest modifyCommentRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            if (comment.getUserAccount().equals(user)) {
                comment.setContent(modifyCommentRequest.getContent());

                return CommentIdResponse.builder()
                        .commentId(commentRepository.save(comment).getCommentId())
                        .build();
            } else {
                throw new BadRequestException(ResponseCode.COMMENT_NO_PERMISSION);
            }

        } else {
            throw new BadRequestException(ResponseCode.COMMENT_NOT_FOUND);
        }
    }

    public CommentIdResponse deleteComment(String accessToken, Long commentId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            // 댓글 내용은 삭제하지만 그 외의 데이터는 모두 남아있음
            // isRemoved가 true인 댓글은 클라이언트에서 "삭제된 댓글입니다" 등으로 표시됨
            if (comment.getUserAccount().equals(user)) {
                if (comment.isRemoved()) {
                    throw new BadRequestException(ResponseCode.COMMENT_ALREADY_REMOVED);
                }

                comment.setContent(null);
                comment.setRemoved(true);

                return CommentIdResponse.builder()
                        .commentId(commentRepository.save(comment).getCommentId())
                        .build();
            } else {
                throw new BadRequestException(ResponseCode.COMMENT_NO_PERMISSION);
            }
        } else {
            throw new BadRequestException(ResponseCode.COMMENT_NOT_FOUND);
        }
    }


    private Comment getCommentEntityById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            log.info("댓글 entity 조회 완료");
            return optionalComment.get();
        } else {
            log.info("댓글 entity 조회 실패 : 해당 ID의 댓글을 찾을 수 없음");
            throw new BadRequestException(ResponseCode.COMMENT_NOT_FOUND);
        }
    }

    public int getCommentCount(Posting posting) {
        return commentRepository.countCommentByPosting(posting);
    }

    public void saveLikeStatus(String accessToken, Long commentId, int likeStatus) {
        if (likeStatus != -1 && likeStatus != 0 && likeStatus != 1) {
            throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
        }

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);


        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            CommentLike commentLike = commentLikeService.findByUserAccountAndE(user, comment);

            // commentLike 신규등록
            if (commentLike == null) {
                commentLikeService.saveLikeStatus(CommentLike.builder()
                        .userAccount(user)
                        .comment(comment)
                        .likeStatus(likeStatus)
                        .build());
            } else {
                // like_status가 1이면 좋아요, -1이면 싫어요, 0이면 둘 중 하나를 취소한 상태이므로 테이블에서 데이터 삭제
                if (likeStatus == 0) {
                    commentLikeService.deleteLikeStatus(commentLike);
                } else {
                    commentLike.setLikeStatus(likeStatus);
                    CommentLike savedCommentLikeStatus = commentLikeService.saveLikeStatus(commentLike);

                    // commentLike entity 저장/수정 실패
                    if (savedCommentLikeStatus == null) {
                        throw new BadRequestException(ResponseCode.LIKE_STATUS_INVALID);
                    }
                }
            }

            // like, dislike count 갱신
            commentRepository.updateLikeCount(comment.getCommentId(), commentLikeService.getLikeCount(comment));
            commentRepository.updateDislikeCount(comment.getCommentId(), commentLikeService.getDislikeCount(comment));
        } else {
            throw new BadRequestException(ResponseCode.COMMENT_NOT_FOUND);
        }
    }
}
