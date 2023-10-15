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
                throw new BadRequestException(ResponseCode.COMMENT_MODIFY_NO_PERMISSION);
            }

        } else {
            throw new BadRequestException(ResponseCode.COMMENT_NOT_FOUND);
        }
    }
}
