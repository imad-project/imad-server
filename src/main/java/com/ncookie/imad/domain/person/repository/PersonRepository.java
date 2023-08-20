package com.ncookie.imad.domain.person.repository;

import com.ncookie.imad.domain.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByPersonId(long id);
}