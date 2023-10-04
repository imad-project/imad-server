package com.ncookie.imad.domain.profile.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.repository.ContentsBookmarkRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class BookmarkService {
    private final ContentsBookmarkRepository contentsBookmarkRepository;

    public Page<ContentsBookmark> findAllByUserAccount(UserAccount userAccount, Pageable pageable) {
        return contentsBookmarkRepository.findAllByUserAccount(userAccount, pageable);
    }

    public boolean existsByUserAccountAndContents(UserAccount userAccount, Contents contents) {
        return contentsBookmarkRepository.existsByUserAccountAndContents(userAccount, contents);
    }

    public ContentsBookmark save(ContentsBookmark contentsBookmark) {
        return contentsBookmarkRepository.save(contentsBookmark);
    }

    public boolean existsById(Long bookmarkId) {
        return contentsBookmarkRepository.existsById(bookmarkId);
    }

    public void deleteByIdAndUserAccount(Long id, UserAccount userAccount) {
        contentsBookmarkRepository.deleteByIdAndUserAccount(id, userAccount);
    }
}
