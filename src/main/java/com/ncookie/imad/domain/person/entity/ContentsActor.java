package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

@Entity
public class ContentsActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsActorId;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;
}
