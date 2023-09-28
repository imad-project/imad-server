package com.ncookie.imad.domain.genre.entity;

import com.ncookie.imad.domain.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Description;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Description("유저별 장르 선호 점수 테이블")
public class UserGenrePreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPreferredGenreId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    // 직접 Genre 테이블과 맺어주지 않고, 필요할 때에만 id를 사용하여 genre 데이터를 얻어오자
    private Long genreId;

    @Setter
    // 장르 선호 점수. 작품 추천 등을 할 때 사용됨
    // TODO: 작품 찜, 리뷰/게시물 좋아요 등을 할 때마다 해당 장르에 점수가 올라가도록 해야함
    private int genreRate;
}
