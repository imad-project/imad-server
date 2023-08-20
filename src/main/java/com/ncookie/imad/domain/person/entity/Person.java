package com.ncookie.imad.domain.person.entity;

import com.ncookie.imad.domain.person.dto.DetailsPerson;
import com.ncookie.imad.domain.user.entity.Gender;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private Long personId;

    private String originalName;
    @Setter private String translatedName;

    private Gender gender;
    private String profilePath;

    public static Person toEntity(DetailsPerson person) {
        return Person.builder()
                .personId(person.getId())
                .originalName(person.getName())
                .gender(person.getGender())
                .profilePath(person.getProfilePath())
                .build();
    }
}
