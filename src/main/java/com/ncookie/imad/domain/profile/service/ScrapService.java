package com.ncookie.imad.domain.profile.service;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.profile.repository.PostingScrapRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class ScrapService {
    private final PostingScrapRepository scrapRepository;

//    public Page<ContentsBookmark> findAllByUserAccount(UserAccount userAccount, Pageable pageable) {
//        return contentsBookmarkRepository.findAllByUserAccount(userAccount, pageable);
//    }
//
//    public ContentsBookmark findByUserAccountAndContents(UserAccount userAccount, Contents contents) {
//        return contentsBookmarkRepository.findByUserAccountAndContents(userAccount, contents);
//    }

    public boolean existsByUserAccountAndContents(UserAccount user, Posting posting) {
        return scrapRepository.existsByUserAccountAndPosting(user, posting);
    }

    public PostingScrap save(PostingScrap postingScrap) {
        return scrapRepository.save(postingScrap);
    }

    public boolean existsById(Long bookmarkId) {
        return scrapRepository.existsById(bookmarkId);
    }

    public void deleteByIdAndUserAccount(Long id, UserAccount userAccount) {
        scrapRepository.deleteByIdAndUserAccount(id, userAccount);
    }
}
