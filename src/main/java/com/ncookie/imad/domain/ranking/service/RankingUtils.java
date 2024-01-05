package com.ncookie.imad.domain.ranking.service;

import org.springframework.context.annotation.Description;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RankingUtils {
    @Description("n일 전까지의 날짜 포맷 스트링을 리스트로 반환. 주간/월간 랭킹 점수를 정산할 때 사용됨")
    public static List<String> getRecentDates(int days) {
        List<String> recentDates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 오늘부터 최근 7일 동안의 날짜 생성
        for (int i = 0; i < days; i++) {
            LocalDate currentDate = LocalDate.now().minusDays(i + 1);
            String formattedDate = currentDate.format(formatter);
            recentDates.add(formattedDate);
        }

        return recentDates;
    }

    @Description("n일 전까지의 날짜 포맷 스트링을 리스트로 반환. 주간/월간 랭킹 점수를 정산할 때 사용됨")
    public static String getLastDate(int n) {
        // 자정이 지났으므로 전날 날짜를 가져옴
        LocalDate currentDate = LocalDate.now().minusDays(n);

        // `20231231` 과 같은 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return currentDate.format(formatter);
    }
}
