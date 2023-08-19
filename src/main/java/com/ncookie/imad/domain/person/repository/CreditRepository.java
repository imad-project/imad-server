package com.ncookie.imad.domain.person.repository;

import com.ncookie.imad.domain.person.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}