package com.ncookie.imad.domain.profile.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsBookmarkRepository extends JpaRepository<ContentsBookmark, Long> {
    Page<ContentsBookmark> findAllByUserAccount(UserAccount userAccount, Pageable pageable);

    ContentsBookmark findByUserAccountAndContents(UserAccount userAccount, Contents contents);
    ContentsBookmark findByIdAndUserAccount(Long id, UserAccount userAccount);

    boolean existsByUserAccountAndContents(UserAccount userAccount, Contents contents);

    void deleteByIdAndUserAccount(Long id, UserAccount userAccount);
}
