package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.repository.ContentsRepository;
import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.entity.RankingAllTime;
import com.ncookie.imad.domain.ranking.entity.RankingBaseEntity;
import com.ncookie.imad.domain.ranking.repository.RankingAllTimeRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.ncookie.imad.domain.ranking.service.RankingUtils.genreStringMap;
import static com.ncookie.imad.domain.ranking.service.RankingUtils.getLastDate;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingSystemService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RankingAllTimeRepository rankingAllTimeRepository;
    private final ContentsRepository contentsRepository;

    public Set<ContentsData> getRankingByContentsType(RankingPeriod rankingPeriod, String contentsTypeString) {
        ContentsType contentsType;
        try {
            contentsType  = ContentsType.valueOf(contentsTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("옳바르지 않은 contents type string으로 인해 에러 발생");
            log.error(e.getMessage());
            throw new BadRequestException(ResponseCode.RANKING_WRONG_CONTENTS_TYPE);
        }

        rankingAllTimeRepository.save(
                RankingAllTime.builder()
                        .contents(null)
                        .rankChanged((long) -2)
                        .rank(5L)
                        .build()
        );

        return getRankingFromRedis(rankingPeriod, genreStringMap.get(contentsType));
    }

    // Redis에서 내림차순으로 랭킹 데이터 Set<ContentsData> 형식으로 반환
    private Set<ContentsData> getRankingFromRedis(RankingPeriod rankingPeriod, String genreString) {
        String todayDate = getLastDate(0);
        String key = rankingPeriod.getValue() + "_ranking" + genreString + todayDate;

        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<Object> objects = zSetOperations.rangeByScore(
                key,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY);
        log.info("랭킹 데이터를 Redis로부터 상위권부터 조회");
        
        if (objects == null) {
            log.error("랭킹 데이터를 찾을 수 없음");
            return null;
        }

        // 데이터의 순서를 유지하기 위해 LinkedHashSet 사용
        Set<ContentsData> contentsDataSet = new LinkedHashSet<>();
        for (Object obj : objects) {
            if (obj instanceof ContentsData) {
                contentsDataSet.add((ContentsData) obj);
            }
        }

        return contentsDataSet;
    }
}
