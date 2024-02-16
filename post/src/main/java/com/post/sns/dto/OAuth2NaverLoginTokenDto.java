package com.post.sns.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // 스네이크케이스와 파싱
@JsonIgnoreProperties(ignoreUnknown = true) // 없는값 무시
public class OAuth2NaverLoginTokenDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private String error;
    private String errorDescription;
}
