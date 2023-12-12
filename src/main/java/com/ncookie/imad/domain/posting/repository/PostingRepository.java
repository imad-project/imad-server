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
    /* 게시글 리스트 전체 조회용 */
    Page<Posting> findAllByCategory(Pageable pageable, int category);
    
    /* 게시글 리스트 조건부 검색용 */
    // 제목 또는 본문에 특정 문자열이 포함되어 있는 경우를 검색
    Page<Posting> findAllByTitleContainingOrContentContaining(Pageable pageable, String title, String content);
    Page<Posting> findAllByTitleContaining(Pageable pageable, String query);
    Page<Posting> findAllByContentContaining(Pageable pageable, String query);
    @Query("SELECT p FROM Posting p WHERE p.user.nickname LIKE %:nickname%")
    Page<Posting> findAllByUserNicknameContaining(Pageable pageable, @Param("nickname") String nickname);

    // 카테고리와 일치하면서, 제목 또는 본문에 특정 문자열이 포함되어 있는 경우를 검색
    Page<Posting> findAllByCategoryAndTitleContainingOrCategoryAndContentContaining(Pageable pageable,
                                                                                    int category1,
                                                                                    String title,
                                                                                    int category2,
                                                                                    String content);
    Page<Posting> findAllByTitleContainingAndCategory(Pageable pageable, String query, int category);
    Page<Posting> findAllByContentContainingAndCategory(Pageable pageable, String query, int category);
    @Query("SELECT p FROM Posting p WHERE p.category = :category AND p.user.nickname LIKE %:nickname%")
    Page<Posting> findAllByCategoryAndUserNicknameContaining(Pageable pageable, @Param("category") int category, @Param("nickname") String nickname);


    // 프로필 조회용
    @Query("SELECT COUNT(*) FROM Posting WHERE user = :user")
    int countWrittenPostingByUser(UserAccount user);

    Page<Posting> findAllByUser(UserAccount user, Pageable pageable);

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
