package com.post.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter // Setter가 있어야함
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
public class Oauth2NaverRegistrationProperties {

    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String clientName;
    private String redirectUri;
    private String scope;
}
