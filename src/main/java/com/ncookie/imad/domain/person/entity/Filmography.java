package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

@Entity
public class Filmography {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmographyId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
