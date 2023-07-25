package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;

@Entity
public class ContentsCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsCrewId;

    @ManyToOne
    @JoinColumn(name = "crew_id")
    private Crew crew;
}
