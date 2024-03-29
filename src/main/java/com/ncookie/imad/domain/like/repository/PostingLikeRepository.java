package com.ncookie.imad.domain.like.repository;

import com.ncookie.imad.domain.like.entity.PostingLike;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostingLikeRepository extends JpaRepository<PostingLike, Long> {
    PostingLike findByUserAccountAndPosting(UserAccount user, Posting posting);

    Page<PostingLike> findAllByUserAccount(UserAccount user, Pageable pageable);
    @Query("SELECT p FROM PostingLike p WHERE p.userAccount = :user AND p.likeStatus = :likeStatus")
    Page<PostingLike> findAllByUserAccountAndLikeStatus(@Param("user") UserAccount user, Pageable pageable, @Param("likeStatus") int likeStatus);

    @Query("SELECT COUNT(*) FROM PostingLike WHERE posting = :posting AND likeStatus = 1")
    int countLikeByPosting(@Param("posting") Posting posting);

    @Query("SELECT COUNT(*) FROM PostingLike WHERE posting = :posting AND likeStatus = -1")
    int countDislikeByPosting(@Param("posting") Posting posting);
}
