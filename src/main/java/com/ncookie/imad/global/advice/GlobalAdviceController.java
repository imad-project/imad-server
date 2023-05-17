package com.ncookie.imad.global.advice;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdviceController {
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<ApiResponse<?>> badRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(ApiResponse.createError(e.getResponseCode()));
    }
}
