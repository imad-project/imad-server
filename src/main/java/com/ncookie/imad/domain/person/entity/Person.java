package com.ncookie.imad.domain.person.entity;

import com.ncookie.imad.domain.user.entity.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @OneToMany(mappedBy = "person")
    private List<Filmography> filmography = new ArrayList<>();

    private String original_name;
    @Setter private String translated_name;

    private Gender gender;
    private String profile_path;
}
