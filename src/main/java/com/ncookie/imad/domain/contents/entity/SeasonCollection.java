package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SeasonCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seasonCollectionId;

    @ManyToOne
    @JoinColumn(name = "tv_program_data_id")
    private TvProgramData tvProgramData;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;
}
