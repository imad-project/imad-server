package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
