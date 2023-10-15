package com.ncookie.imad.domain.posting.service;

import com.ncookie.imad.domain.posting.dto.request.AddCommentRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyCommentRequest;
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
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserAccountService userAccountService;
    private final PostingService postingService;


    public CommentIdResponse addComment(String accessToken, AddCommentRequest addCommentRequest) {
        UserAccount user = userAccountService.getUserFromAccessToken(accessToken);
        Optional<Posting> postingOptional = postingService.getPostingEntityById(addCommentRequest.getPostingId());

        if (postingOptional.isPresent()) {
            Comment comment = commentRepository.save(
                    Comment.builder()
                            .posting(postingOptional.get())
                            .userAccount(user)

                            .parentId(addCommentRequest.getParentId())
                            .content(addCommentRequest.getContent())

                            .build()
            );

            return CommentIdResponse.builder()
                    .commentId(comment.getCommentId())
                    .build();
        } else {
            throw new BadRequestException(ResponseCode.POSTING_NOT_FOUND);
        }
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
}
