package com.ncookie.imad.domain.contents.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Broadcaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long broadcasterId;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private TvProgramData tvProgramData;

    @ManyToOne
    @JoinColumn(name = "networks_id")
    private Networks networks;
}
