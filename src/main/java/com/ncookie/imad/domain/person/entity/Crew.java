package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewId;

    @OneToMany(mappedBy = "crew")
    private List<ContentsCrew> contentsActors = new ArrayList<>();
}
