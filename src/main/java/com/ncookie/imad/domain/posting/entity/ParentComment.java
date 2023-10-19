package com.ncookie.imad.domain.posting.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParentComment extends Comment {
    private int childrenNum;
}
