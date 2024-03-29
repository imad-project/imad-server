package com.ncookie.imad.domain.person.entity;

import com.ncookie.imad.domain.contents.entity.Contents;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Credit {
    @Id
    private String creditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id")
    @ToString.Exclude
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contents_id")
    @ToString.Exclude
    private Contents contents;


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
    
    @Setter
    // credit의 타입이 cast(배우)인지, crew(스태프)인지 구분하기 위한 칼럼
    private CreditType creditType;

    @Setter
    private int importanceOrder;
}
