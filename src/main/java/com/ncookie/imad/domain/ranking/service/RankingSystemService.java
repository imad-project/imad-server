package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.ranking.dto.ContentsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingSystemService {
    private final RedisTemplate<String, Object> redisTemplate;

    private List<ContentsData> getRankingFromRedis() {
        return null;
    }
}
