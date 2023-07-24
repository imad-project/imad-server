package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("tv")
@Entity
public class TvProgramData extends Contents {
    @Setter private LocalDate createdDate;
    @Setter private LocalDate modifiedDate;

    @Setter private int numberOfEpisodes;
    @Setter private int numberOfSeasons;

    @ManyToMany
    @JoinTable(name = "broadcaster")
    @ToString.Exclude
    private List<Networks> networks = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "season_collection")
    @ToString.Exclude
    private List<Season> seasonCollection = new ArrayList<>();

    public void setNetworks(List<Networks> networks) {
        this.networks = networks;
    }
}
