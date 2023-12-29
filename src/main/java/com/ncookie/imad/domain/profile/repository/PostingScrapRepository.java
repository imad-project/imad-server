package com.ncookie.imad.domain.profile.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostingScrapRepository extends JpaRepository<PostingScrap, Long> {
    Page<PostingScrap> findAllByUserAccount(UserAccount userAccount, Pageable pageable);

    PostingScrap findByUserAccountAndPosting(UserAccount userAccount, Posting posting);

    PostingScrap findByIdAndUserAccount(Long id, UserAccount userAccount);

    // 프로필 조회용
    @Query("SELECT COUNT(*) FROM PostingScrap WHERE userAccount = :user")
    int countPostingScrapByUser(UserAccount user);

    boolean existsByUserAccountAndPosting(UserAccount userAccount, Posting posting);

    void deleteByIdAndUserAccount(Long id, UserAccount userAccount);
}