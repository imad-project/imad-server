package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.PostingBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingBookmarkRepository extends JpaRepository<PostingBookmark, Long> {
}