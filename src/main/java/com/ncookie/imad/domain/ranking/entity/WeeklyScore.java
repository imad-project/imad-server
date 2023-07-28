package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WeeklyScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyScoreId;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;

    private LocalDate refDate;

    @Setter
    private int rankingScore;
}
