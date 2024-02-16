package com.post.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter // Setter가 있어야함
@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider.naver")
public class Oauth2NaverProviderProperties {
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String userNameAttribute;
}
