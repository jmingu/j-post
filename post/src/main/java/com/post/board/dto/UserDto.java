package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long userId;

    private String loginId;

    private String userName;

    private String email;

    private String gender;

    private String loginType;

    private String nickname;
}
