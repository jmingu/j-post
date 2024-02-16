package com.post.sns.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginTokenDto {

    private String accessToken;
    private String refreshToken;

}
