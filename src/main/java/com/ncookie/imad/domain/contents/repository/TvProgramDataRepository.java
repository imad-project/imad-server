package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TvProgramDataRepository extends JpaRepository<TvProgramData, Long> {
    TvProgramData findByContentsId(Long id);

    /*
     * 작품 검색용
     */
    @Query("SELECT DISTINCT tv FROM TvProgramData tv " +
            "LEFT JOIN tv.contentsGenres g " +
            "LEFT JOIN tv.productionCountries c " +
            "WHERE tv.contentsType = :contentsType " +
            "AND (:query IS NULL OR LOWER(tv.translatedTitle) LIKE LOWER(concat('%', :query, '%'))) " +
            "AND (:genreList IS NULL OR g IN :genreList) " +
            "AND (:countryList IS NULL OR c IN :countryList) " +
            "AND tv.firstAirDate >= :releaseDateMin " +
            "AND tv.firstAirDate <= :releaseDateMax " +

            // isNullScoreOk == true일 때 : 평점이 null인 경우 또는 평점이 min과 max 사이인 경우를 조회
            "AND (:isNullScoreOk = true AND (tv.imadScore IS NULL OR (tv.imadScore >= :scoreMin AND tv.imadScore <= :scoreMax))) " +
            // isNullScoreOk == false일 때 : 평점이 null이 아니고 평점이 min과 max 사이인 경우를 조회
            "OR (:isNullScoreOk = false AND tv.imadScore IS NOT NULL AND tv.imadScore >= :scoreMin AND tv.imadScore <= :scoreMax) ")

    Page<TvProgramData> searchTvProgramData(Pageable pageable,
                                            @Param("query") String query,
                                            @Param("contentsType") ContentsType contentsType,
                                            @Param("genreList") List<Integer> genreList,
                                            @Param("countryList") List<String> countryList,
                                            @Param("releaseDateMin") LocalDate releaseDateMin,
                                            @Param("releaseDateMax") LocalDate releaseDateMax,
                                            @Param("isNullScoreOk") boolean isNullScoreOk,
                                            @Param("scoreMin") Float scoreMin,
                                            @Param("scoreMax") Float scoreMax);

    Page<TvProgramData> findAllByTranslatedTitleContaining(Pageable pageable, String query);
}
