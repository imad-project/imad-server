package com.ncookie.imad.domain.networks.repository;

import com.ncookie.imad.domain.networks.entity.Broadcaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcasterRepository extends JpaRepository<Broadcaster, Long> {
}