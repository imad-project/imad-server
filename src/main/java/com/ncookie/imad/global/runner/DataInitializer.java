package com.ncookie.imad.global.runner;

import com.ncookie.imad.domain.genre.entity.Genre;
import com.ncookie.imad.domain.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final GenreRepository genreRepository;

    // Genre 테이블에 들어가는 데이터는 TMDB 측에서 업데이트를 하지 않는 이상 항상 고정이다.
    // 그렇기 때문에 서버가 실행될 때마다 기존 데이터 유무를 체크하여 미리 설정된 데이터로 초기화해준다.
    @Override
    public void run(ApplicationArguments args) {
        final int GENRE_TYPE_COMMON = 0;
        final int GENRE_TYPE_TV = 1;
        final int GENRE_TYPE_MOVIE = 2;

        List<Integer> commonGenreIds = Arrays.asList(
                16, 18, 35, 37, 80, 99, 9648, 10751
        );
        List<Integer> tvGenreIds = Arrays.asList(
                10759, 10762, 10763, 10764, 10765, 10766, 10767, 10768
        );
        List<Integer> movieGenreIds = Arrays.asList(
                12, 14, 27, 28, 36, 53, 878, 10402, 10749, 10752, 10770
        );

        List<String> commonGenreNames = Arrays.asList(
                "애니메이션", "드라마", "코미디", "서부", "범죄", "다큐멘터리", "미스터리", "가족"
        );
        List<String> tvGenreNames = Arrays.asList(
                "액션&어드벤쳐", "아동", "뉴스", "리얼리티", "SF/판타지", "소프 오페라", "토크", "전쟁/정치"
        );
        List<String> movieGenreNames = Arrays.asList(
                "어드벤쳐", "판타지", "공포", "액션", "역사", "스릴러", "SF", "음악", "로맨스", "전쟁", "TV영화"
        );


        // 초기 데이터가 저장되어 있지 않을 때에만 데이터 초기화 실행
        if (genreRepository.count() == 0) {
            for (int i = 0; i < commonGenreIds.size(); i++) {
                genreRepository.save(Genre.builder()
                        .genreId(Long.valueOf(commonGenreIds.get(i)))
                        .name(commonGenreNames.get(i))
                        .genreType(GENRE_TYPE_COMMON)
                        .build()
                );
            }

            for (int i = 0; i < tvGenreIds.size(); i++) {
                genreRepository.save(Genre.builder()
                        .genreId(Long.valueOf(tvGenreIds.get(i)))
                        .name(tvGenreNames.get(i))
                        .genreType(GENRE_TYPE_TV)
                        .build()
                );
            }

            for (int i = 0; i < movieGenreIds.size(); i++) {
                 genreRepository.save(Genre.builder()
                         .genreId(Long.valueOf(movieGenreIds.get(i)))
                         .name(movieGenreNames.get(i))
                         .genreType(GENRE_TYPE_MOVIE)
                         .build()
                 );
            }
        }
    }
}
