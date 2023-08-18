package com.ncookie.imad.domain.networks.service;


import com.ncookie.imad.domain.networks.entity.Broadcaster;
import com.ncookie.imad.domain.networks.entity.Networks;
import com.ncookie.imad.domain.networks.repository.BroadcasterRepository;
import com.ncookie.imad.domain.networks.repository.NetworksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class NetworksService {
    private final NetworksRepository networksRepository;
    private final BroadcasterRepository broadcasterRepository;

    public Networks saveNetworksInfo(Networks networks) {
        return networksRepository.save(networks);
    }

    public void saveBroadcaster(Broadcaster broadcaster) {
        broadcasterRepository.save(broadcaster);
    }
}
