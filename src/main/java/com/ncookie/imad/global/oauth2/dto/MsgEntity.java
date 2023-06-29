package com.ncookie.imad.global.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgEntity {

    private String id;
    private Object result;

    public MsgEntity(String id, Object result) {
        this.id = id;
        this.result  = result;
    }
}