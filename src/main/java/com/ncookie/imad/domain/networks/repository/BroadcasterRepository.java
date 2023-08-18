package com.ncookie.imad.domain.networks.repository;

import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.networks.entity.Broadcaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BroadcasterRepository extends JpaRepository<Broadcaster, Long> {
    List<Broadcaster> findAllByTvProgramData(TvProgramData tvProgramData);
}