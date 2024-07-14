package com.ncookie.imad.global.advice;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.naming.SizeLimitExceededException;

@RestControllerAdvice
public class GlobalAdviceController {
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<ApiResponse<?>> badRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(ApiResponse.createError(e.getResponseCode()));
    }

    @ExceptionHandler(value = MultipartException.class)
    protected ResponseEntity<ApiResponse<?>> fileSizeLimitExceededException() {
        return ResponseEntity.badRequest().body(ApiResponse.createError(ResponseCode.FILE_MAX_UPLOAD_SIZE_EXCEEDED));
    }
}
