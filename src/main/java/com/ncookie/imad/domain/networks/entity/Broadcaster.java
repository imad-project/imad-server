package com.ncookie.imad.domain.networks.entity;


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
public class Broadcaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long broadcasterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private TvProgramData tvProgramData;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "networks_id")
    @ToString.Exclude
    private Networks networks;
}
