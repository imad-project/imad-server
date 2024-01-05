package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.ranking.dto.ContentsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ncookie.imad.domain.ranking.service.RankingUtils.getLastDate;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingSystemService {
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis에서 내림차순으로 랭킹 데이터 Set<ContentsData> 형식으로 반환
    private Set<ContentsData> getRankingFromRedis(String periodString, String genreString) {
        String todayDate = getLastDate(0);
        String key = periodString + "_ranking" + genreString + todayDate;

        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<Object> objects = zSetOperations.reverseRangeByScore(
                key,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY);
        
        if (objects == null) {
            log.error("랭킹 데이터를 찾을 수 없음");
            return null;
        }

        return objects.stream()
                .filter(ContentsData.class::isInstance)
                .map(ContentsData.class::cast)
                .collect(Collectors.toSet());
    }
}
