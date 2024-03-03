package com.ncookie.imad.domain.contents.service;

import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.contents.repository.ContentsRepository;
import com.ncookie.imad.domain.contents.repository.MovieDataRepository;
import com.ncookie.imad.domain.contents.repository.TvProgramDataRepository;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class ContentsService {
    private final TmdbApiClient apiClient;

    private final ContentsRepository contentsRepository;

    private final MovieDataRepository movieDataRepository;
    private final TvProgramDataRepository tvProgramDataRepository;

    // 작품 검색
    public ContentsSearchResponse searchKeywords(String query, String type, int page) {
        return apiClient.searchByQuery(query, type, page);
    }

    public TmdbDetails fetchContentsDetails(Long id, ContentsType type) {
        return apiClient.fetchContentsDetails(id, type);
    }

    public String fetchContentsCertification(Long id, ContentsType type) {
        return apiClient.fetchContentsCertification(id, type);
    }

    // 프로필에서 작품 북마크 관련으로 사용하는 메소드
    public Contents getContentsEntityById(Long id) {
        Optional<Contents> contents = contentsRepository.findById(id);

        if (contents.isPresent()) {
            return contents.get();
        } else {
            throw new BadRequestException(ResponseCode.CONTENTS_ID_NOT_FOUND);
        }
    }


    /*
     * =====================================
     * Repository wrapper 메소드
     * =====================================
     */

    public Contents getContentsByTmdbIdAndTmdbType(Long id, ContentsType type) {
        return contentsRepository.findByTmdbIdAndTmdbType(id, type);
    }

    public Optional<Contents> getContentsByContentsId(Long id) {
        return contentsRepository.findById(id);
    }

    public MovieData saveMovieData(MovieData movieData) {
        return movieDataRepository.save(movieData);
    }

    // SeasonCollection, Broadcast 같은 연결테이블에 데이터를 추가하기 위해서는 save 이후의 entity 객체가 필요함
    // 때문에 위의 saveMovieData() 메소드와 다르게 TvProgramData entity를 리턴함
    public TvProgramData saveTvData(TvProgramData tvProgramData) {
        return tvProgramDataRepository.save(tvProgramData);
    }

    public boolean checkDuplicationExists(Long id, ContentsType type) {
        return contentsRepository.existsByTmdbIdAndTmdbType(id, type);
    }

    public TvProgramData findTvProgramDataByContentsId(Long id) {
        return tvProgramDataRepository.findByContentsId(id);
    }

    public MovieData findMovieDataByContentsId(Long id) {
        return movieDataRepository.findByContentsId(id);
    }


    // 리뷰 관련
    public void saveContentsScoreAndReviewCount(Contents contents) {
        contentsRepository.save(contents);
    }

    /*
     * 작품 검색용
     */
    public Page<TvProgramData> searchTvProgramData() {
        Sort sort = Sort.by("contentsId").descending();
        PageRequest pageRequest = PageRequest.of(0, Utils.PAGE_SIZE, sort);
        Page<TvProgramData> ingB =
                tvProgramDataRepository.findAllByTranslatedTitleContaining(pageRequest, "");
        Page<TvProgramData> tvProgramData =
                tvProgramDataRepository.searchTvProgramData(
                        pageRequest,
                        "",
                        ContentsType.TV,
                        null,
                        null,
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2500, 1, 1),
                        0.0F,
                        10.0F,
                        true
                        );
        return null;
    }
}
