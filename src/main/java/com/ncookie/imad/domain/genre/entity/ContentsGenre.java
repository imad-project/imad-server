package com.ncookie.imad.domain.genre.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Description;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Description("작품 별 장르")
public class ContentsGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsGenreId;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;
}
