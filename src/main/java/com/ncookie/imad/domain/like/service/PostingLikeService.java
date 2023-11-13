package com.ncookie.imad.domain.like.service;

import com.ncookie.imad.domain.like.entity.PostingLike;
import com.ncookie.imad.domain.like.repository.PostingLikeRepository;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class PostingLikeService implements LikeService<Posting, PostingLike> {
    private final PostingLikeRepository postingLikeRepository;

    @Override
    public PostingLike findByUserAccountAndE(UserAccount user, Posting posting) {
        return postingLikeRepository.findByUserAccountAndPosting(user, posting);
    }

    @Override
    public PostingLike saveLikeStatus(PostingLike like) {
        return postingLikeRepository.save(like);
    }

    @Override
    public void deleteLikeStatus(PostingLike like) {
        postingLikeRepository.delete(like);
    }

    @Override
    public int getLikeCount(Posting posting) {
        return postingLikeRepository.countLikeByPosting(posting);
    }

    @Override
    public int getDislikeCount(Posting posting) {
        return postingLikeRepository.countDislikeByPosting(posting);
    }

    @Override
    public Page<PostingLike> getLikedListByUser(UserAccount user, Pageable pageable, int likeStatus) {
        // likeStatus가 0이면 전체조회, 1이면 좋아요, -1이면 싫어요 등록한 게시글 리스트를 조회함
        if (likeStatus == 0) {
            return postingLikeRepository.findAllByUserAccount(user, pageable);
        } else {
            return postingLikeRepository.findAllByUserAccountAndLikeStatus(user, pageable, likeStatus);
        }
    }
}
