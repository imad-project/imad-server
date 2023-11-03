package com.ncookie.imad.domain.season.entity;

import com.ncookie.imad.domain.contents.entity.TvProgramData;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tv_program_data_id")
    @ToString.Exclude
    private TvProgramData tvProgramData;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "season_id")
    @ToString.Exclude
    private Season season;
}
