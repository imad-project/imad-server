package com.ncookie.imad.domain.like.service;

import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


// E : Review, Posting과 같은 Entity, L : ReviewLike, PostingLike와 같은 Entity
public interface LikeService<E, L> {

    // Entity(ReviewLike, PostingLike 등)과 User 계정 사용하여 like status 얻어오기 
    L findByUserAccountAndE(UserAccount user, E entity);

    // like status 저장
    L saveLikeStatus(L like);

    // like status 삭제
    void deleteLikeStatus(L like);

    // 해당 entity의 like 개수 count
    int getLikeCount(E entity);

    // 해당 entity의 dislike 개수 count
    int getDislikeCount(E entity);

    // 유저가 like 또는 dislike 한 Entity(리뷰, 게시글 등) 리스트 조회
    Page<L> getLikedListByUser(UserAccount user, Pageable pageable);
}
