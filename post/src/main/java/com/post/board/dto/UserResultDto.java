package com.post.board.dto;

import lombok.Getter;

@Getter
public class UserResultDto {
    private int resultCode;
    private String resultMessage;
    private UserDto result;
}
