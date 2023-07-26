package com.ncookie.imad.domain.person.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Filmography {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmographyId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Setter
    // 담당부서
    // 감독이나 작가, 기타 스태프 등의 인물만 가지고 있는 속성임
    // Ex) Production, Wriging, Camera, etc.
    private String department;

    @Setter
    // 특정 부서 또는 분야로 잘 알려져 있는 정보
    // Ex) Acting, Writing, Camera, Crew, etc.
    private String knownForDepartment;

    @Setter
    // 맡은 업무
    // Ex) Co-Executive Producer, Production Coordinaotr, Camera Operator, etc.
    private String job;

    @Setter
    // 배역명. 배우들만 해당됨
    private String characterName;
}
