package com.ncookie.imad.global.exception;

import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private final ResponseCode responseCode;

//    public BadRequestException(String message) {
//        super(message);
//    }
//
//    public BadRequestException(String message, Throwable cause) {
//        super(message, cause);
//    }

}
