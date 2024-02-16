package com.post.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(HttpStatus.OK.value(), "SUCCESS"),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT.value(), "User name is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "User not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "Passeord is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Token is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Post not founded"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED.value(), "Permission is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),;

    private int code;
    private String message;
}
