package com.ncookie.imad.domain.networks.service;


import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.networks.entity.Broadcaster;
import com.ncookie.imad.domain.networks.entity.Networks;
import com.ncookie.imad.domain.networks.repository.BroadcasterRepository;
import com.ncookie.imad.domain.networks.repository.NetworksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NetworksService {
    private final NetworksRepository networksRepository;
    private final BroadcasterRepository broadcasterRepository;

    public Networks saveNetworksInfo(Networks networks) {
        return networksRepository.save(networks);
    }

    public void saveBroadcaster(Networks networks, TvProgramData tvProgramData) {
        broadcasterRepository.save(
                Broadcaster.builder()
                    .networks(networks)
                    .tvProgramData(tvProgramData)
                    .build());
    }

    public List<Networks> getNetworksEntities(TvProgramData tvProgramData) {
        List<Broadcaster> allByTvProgramData1 = broadcasterRepository.findAllByTvProgramData(tvProgramData);

        List<Networks> networksList = new ArrayList<>();
        allByTvProgramData1.forEach(broadcaster -> networksList.add(broadcaster.getNetworks()));

        return networksList;
    }
}
