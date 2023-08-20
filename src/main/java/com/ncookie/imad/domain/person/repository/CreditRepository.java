package com.ncookie.imad.domain.person.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.person.entity.Credit;
import com.ncookie.imad.domain.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByContents(Contents contents);
}