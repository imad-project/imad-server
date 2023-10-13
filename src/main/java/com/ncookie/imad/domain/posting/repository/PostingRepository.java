package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    Page<Posting> findAllByTitleOrContentContaining(Pageable pageable, String title, String content);
    Page<Posting> findAllByTitleContaining(Pageable pageable, String query);
    Page<Posting> findAllByContentContaining(Pageable pageable, String query);

    @Query("SELECT p FROM Posting p WHERE p.user.nickname LIKE %:nickname%")
    Page<Posting> findAllByUserNicknameContaining(Pageable pageable, @Param("nickname") String nickname);
}
