package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.PostingLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingLikeRepository extends JpaRepository<PostingLike, Long> {
}