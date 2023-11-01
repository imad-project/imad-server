package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    Page<Posting> findAllByTitleContainingOrContentContaining(Pageable pageable, String title, String content);
    Page<Posting> findAllByTitleContaining(Pageable pageable, String query);
    Page<Posting> findAllByContentContaining(Pageable pageable, String query);
    
    // 프로필 조회용
    Page<Posting> findAllByUser(UserAccount user, Pageable pageable);

    @Query("SELECT p FROM Posting p WHERE p.user.nickname LIKE %:nickname%")
    Page<Posting> findAllByUserNicknameContaining(Pageable pageable, @Param("nickname") String nickname);

    @Modifying
    @Query("UPDATE Posting SET likeCnt = :likeCnt WHERE postingId = :postingId")
    void updateLikeCount(@Param("postingId") Long postingId, @Param("likeCnt") int likeCnt);

    @Modifying
    @Query("UPDATE Posting SET dislikeCnt = :dislikeCnt WHERE postingId = :postingId")
    void updateDislikeCount(@Param("postingId") Long postingId, @Param("dislikeCnt") int dislikeCnt);

    @Modifying
    @Query("UPDATE Posting SET viewCnt = :viewCnt WHERE postingId = :postingId")
    void updateViewCount(@Param("postingId") Long postingId, @Param("viewCnt") int viewCnt);
}
