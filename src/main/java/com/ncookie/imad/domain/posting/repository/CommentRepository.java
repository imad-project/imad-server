package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.posting.entity.Posting;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostingAndParentNull(Posting posting, Pageable pageable);
    Page<Comment> findAllByPostingAndParent(Posting posting, Comment parent, Pageable pageable);

    @Query("SELECT COUNT(*) FROM Comment WHERE isRemoved = false")
    int countCommentByPosting(Posting posting);

    int countCommentByParent(Comment comment);

    @Modifying
    @Query("UPDATE Comment SET likeCnt = :likeCnt WHERE commentId = :commentId")
    void updateLikeCount(@Param("commentId") Long commentId, @Param("likeCnt") int likeCnt);

    @Modifying
    @Query("UPDATE Comment SET dislikeCnt = :dislikeCnt WHERE commentId = :commentId")
    void updateDislikeCount(@Param("commentId") Long commentId, @Param("dislikeCnt") int dislikeCnt);
}
