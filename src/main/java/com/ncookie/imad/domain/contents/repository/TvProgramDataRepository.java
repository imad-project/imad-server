package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TvProgramDataRepository extends JpaRepository<TvProgramData, Long>, JpaSpecificationExecutor {
    TvProgramData findByContentsId(Long id);

    /*
     * 작품 검색용
     */
    @Query("SELECT DISTINCT tv FROM TvProgramData tv " +
            "LEFT JOIN tv.contentsGenres g " +
            "LEFT JOIN tv.productionCountries c " +

            // 작품 타입별
            "WHERE tv.contentsType = :contentsType " +

            // 대소문자 구분없이 검색
            "AND (:query IS NULL OR LOWER(tv.translatedTitle) LIKE LOWER(concat('%', :query, '%'))) " +

            // 장르 검색
            "AND (:genreList IS NULL OR g IN :genreList) " +

            // 제작 국가 검색
            "AND (:countryList IS NULL OR c IN :countryList) " +

            // 최초 방영일이 특정 연도 안에 있을 때만 조회
            "AND tv.firstAirDate >= :releaseDateMin " +
            "AND tv.firstAirDate <= :releaseDateMax " +

            // isNullScoreOk == true일 때 : 평점이 null인 경우 또는 평점이 min과 max 사이인 경우를 조회
            "AND (:isNullScoreOk = true AND (tv.imadScore IS NULL OR (tv.imadScore >= :scoreMin AND tv.imadScore <= :scoreMax))) " +
            // isNullScoreOk == false일 때 : 평점이 null이 아니고 평점이 min과 max 사이인 경우를 조회
            "OR (:isNullScoreOk = false AND tv.imadScore IS NOT NULL AND tv.imadScore >= :scoreMin AND tv.imadScore <= :scoreMax) ")
    Page<TvProgramData> searchTvProgramData(Pageable pageable,
                                            @Param("query") String query,
                                            @Param("contentsType") ContentsType contentsType,
                                            @Param("genreList") Set<Integer> genreList,
                                            @Param("countryList") List<String> countryList,
                                            @Param("releaseDateMin") LocalDate releaseDateMin,
                                            @Param("releaseDateMax") LocalDate releaseDateMax,
                                            @Param("scoreMin") Float scoreMin,
                                            @Param("scoreMax") Float scoreMax,
                                            @Param("isNullScoreOk") boolean isNullScoreOk);

    Page<TvProgramData> findAllByTranslatedTitleContaining(Pageable pageable, String query);

    default List<TvProgramData> searchTv(ContentsType contentsType,
                                                   String query,
                                                   Set<Integer> genreList,
                                                   List<String> countryList,
                                                   LocalDate releaseDateMin,
                                                   LocalDate releaseDateMax,
                                                   Float scoreMin,
                                                   Float scoreMax,
                                                   boolean isNullScoreOk) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TvProgramData> criteriaQuery = criteriaBuilder.createQuery(TvProgramData.class);
        Root<TvProgramData> tvRoot = criteriaQuery.from(TvProgramData.class);

        criteriaQuery.select(tvRoot).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (contentsType != null) {
            predicates.add(criteriaBuilder.equal(tvRoot.get("contentsType"), contentsType));
        }

        if (query != null && !query.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tvRoot.get("translatedTitle")), "%" + query.toLowerCase() + "%"));
        }

        if (genreList != null && !genreList.isEmpty()) {
            Join<TvProgramData, Integer> genreJoin = tvRoot.join("contentsGenres");
            predicates.add(genreJoin.get("id").in(genreList));
        }

        if (countryList != null && !countryList.isEmpty()) {
            Join<TvProgramData, String> countryJoin = tvRoot.join("productionCountries");
            predicates.add(countryJoin.in(countryList));
        }

        if (releaseDateMin != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(tvRoot.get("firstAirDate"), releaseDateMin));
        }

        if (releaseDateMax != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(tvRoot.get("firstAirDate"), releaseDateMax));
        }

        if (isNullScoreOk) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(tvRoot.get("imadScore")),
                    criteriaBuilder.between(tvRoot.get("imadScore"), scoreMin, scoreMax)
            ));
        } else {
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.isNotNull(tvRoot.get("imadScore")),
                    criteriaBuilder.between(tvRoot.get("imadScore"), scoreMin, scoreMax)
            ));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
