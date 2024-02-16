package com.post.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nidNaver", url = "https://nid.naver.com")
public interface NidNaverFeignClient {
    @GetMapping("/oauth2.0/token")
    ResponseEntity<String> getToken(@RequestParam("grant_type") String grantType,
                                    @RequestParam("client_id") String clientId,
                                    @RequestParam("client_secret") String clientSecret,
                                    @RequestParam("code") String code,
                                    @RequestParam("state") String state);
}


