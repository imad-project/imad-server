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
@Description("유저 별 선호 장르")
public class UserPreferredGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPreferredGenreId;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Setter
    // 장르 선호 점수. 작품 추천 등을 할 때 사용됨
    private int rate;
}
