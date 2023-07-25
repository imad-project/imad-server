package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actorId;

    @OneToMany(mappedBy = "actor")
    private List<ContentsActor> contentsActors = new ArrayList<>();
}
