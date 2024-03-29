package com.ncookie.imad.domain.person.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.person.entity.CreditType;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// 배우, 감독, 작가, 스태프 등의 정보를 가지고 있는 DTO 클래스
public class DetailsPerson {
    // person id. 인물 자체에 대한 id
    private long id;

    // credit id. 작품과 인물 간의 연결점인 credit의 id
    private String creditId;

    private String name;
    private Gender gender;
    private String profilePath;

    private String character;

    private String knownForDepartment;
    private String department;
    private String job;

    // 많은 직책을 맡을수록 작품의 대표인물, 네임드가 될 확률이 높을 것이라고 판단했다.
    // 클라이언트 화면에서 작품정보의 상단에 표시되는 인물을 구분하기 위해서 이 변수를 만들었다.
    // crew 데이터의 리스트에서 중복될수록 카운트를 +1 한다.
    private int importanceOrder;

    // 해당 인물이 cast(배우)인지, crew(감독, 작가, PD 등)인지 구분하기 위한 변수
    private CreditType creditType;

    @JsonCreator
    public DetailsPerson(@JsonProperty("gender") int gender) {
        if (gender == 1) {
            this.gender = Gender.FEMALE;
        } else if (gender == 2) {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.NONE;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof DetailsPerson person)) return false;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
