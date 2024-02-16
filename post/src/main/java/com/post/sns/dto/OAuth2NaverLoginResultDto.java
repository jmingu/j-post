package com.post.sns.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 없는값 무시
public class OAuth2NaverLoginResultDto {
    private String resultcode;
    private String message;
    private OAuth2NaverLoginResponseDto response;

}
