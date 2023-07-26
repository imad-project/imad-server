package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @OneToMany(mappedBy = "person")
    private List<Filmography> filmography = new ArrayList<>();
}
