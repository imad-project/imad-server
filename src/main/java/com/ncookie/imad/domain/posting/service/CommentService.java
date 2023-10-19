package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.posting.dto.request.AddCommentRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyCommentRequest;
import com.ncookie.imad.domain.posting.dto.response.CommentDetailsResponse;
import com.ncookie.imad.domain.posting.dto.response.CommentIdResponse;
import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.repository.CommentRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserAccountService userAccountService;
    private final PostingRetrievalService postingRetrievalService;


    public CommentDetailsResponse getComment(Long commentId) {
        Comment comment = getCommentEntityById(commentId);
        return CommentDetailsResponse.toDTO(comment);
    }

    public CommentIdResponse addComment(String accessToken, AddCommentRequest addCommentRequest) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
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
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
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
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
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


    public List<CommentDetailsResponse> getCommentList(Posting posting) {
        List<Comment> comments = commentRepository.findAllByPosting(posting);

        List<CommentDetailsResponse> commentList = new ArrayList<>();
        for (Comment c : comments) {
            commentList.add(CommentDetailsResponse.toDTO(c));
        }

        return commentList;
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
}
