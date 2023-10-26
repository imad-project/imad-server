package com.ncookie.imad.domain.like.repository;

import com.ncookie.imad.domain.like.entity.CommentLike;
import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByUserAccountAndComment(UserAccount user, Comment comment);

    Page<CommentLike> findAllByUserAccount(UserAccount user, Pageable pageable);

    @Query("SELECT COUNT(*) FROM CommentLike WHERE comment = :comment AND likeStatus = 1")
    int countLikeByComment(@Param("comment") Comment comment);

    @Query("SELECT COUNT(*) FROM CommentLike WHERE comment = :comment AND likeStatus = -1")
    int countDislikeByComment(@Param("comment") Comment comment);
}
