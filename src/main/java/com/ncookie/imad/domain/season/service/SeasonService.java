package com.ncookie.imad.domain.season.service;

import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.season.entity.Season;
import com.ncookie.imad.domain.season.entity.SeasonCollection;
import com.ncookie.imad.domain.season.repository.SeasonCollectionRepository;
import com.ncookie.imad.domain.season.repository.SeasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final SeasonCollectionRepository seasonCollectionRepository;

    public Season saveSeasonInfo(Season season) {
        return seasonRepository.save(season);
    }

    public void saveSeasonCollection(Season season, TvProgramData tvProgramData) {
        SeasonCollection build = SeasonCollection.builder()
                .season(season)
                .tvProgramData(tvProgramData)
                .build();

        seasonCollectionRepository.save(build);
    }

    public List<Season> getSeasonsEntities(TvProgramData tvProgramData) {
        List<SeasonCollection> allByTvProgramData = seasonCollectionRepository.findAllByTvProgramData(tvProgramData);

        List<Season> seasons = new ArrayList<>();
        allByTvProgramData.forEach(seasonCollection -> seasons.add(seasonCollection.getSeason()));

        return seasons;
    }
}
