package com.ncookie.imad.domain.networks.repository;

import com.ncookie.imad.domain.networks.entity.Networks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworksRepository extends JpaRepository<Networks, Long> {
}