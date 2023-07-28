package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Broadcaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcasterRepository extends JpaRepository<Broadcaster, Long> {
}