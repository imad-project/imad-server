package com.ncookie.imad.domain.like.service;

import com.ncookie.imad.domain.like.entity.CommentLike;
import com.ncookie.imad.domain.like.repository.CommentLikeRepository;
import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class CommentLikeService implements LikeService<Comment, CommentLike> {
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public CommentLike findByUserAccountAndE(UserAccount user, Comment comment) {
        return commentLikeRepository.findByUserAccountAndComment(user, comment);
    }

    @Override
    public CommentLike saveLikeStatus(CommentLike like) {
        return commentLikeRepository.save(like);
    }

    @Override
    public void deleteLikeStatus(CommentLike like) {
        commentLikeRepository.delete(like);
    }

    @Override
    public int getLikeCount(Comment comment) {
        return commentLikeRepository.countLikeByComment(comment);
    }

    @Override
    public int getDislikeCount(Comment comment) {
        return commentLikeRepository.countDislikeByComment(comment);
    }

    @Override
    public Page<CommentLike> getLikedListByUser(UserAccount user, Pageable pageable, int likeStatus) {
        // 좋아요/싫어요 등록한 댓글 리스트 조회 기능은 없기 때문에 likeStatus 변수는 사용되지 않음
        return commentLikeRepository.findAllByUserAccount(user, pageable);
    }
}
