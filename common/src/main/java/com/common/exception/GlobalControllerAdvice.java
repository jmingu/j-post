package com.common.exception;


import com.common.dto.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(JApplicationException.class)
    public ResponseEntity<?> applicationHandler(JApplicationException e) {
        log.error(e.getResultMessage());
        return CommonResponseDto.error(e.getResultCode(), e.getResultMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return CommonResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "server error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e) {
        e.printStackTrace();
        return CommonResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "server error");
    }
}
