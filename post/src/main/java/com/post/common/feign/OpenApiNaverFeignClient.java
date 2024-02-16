package com.post.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openapiNaver", url = "https://openapi.naver.com")
public interface OpenApiNaverFeignClient {
    @GetMapping("/v1/nid/me")
    ResponseEntity<String> getResult(@RequestHeader("Authorization") String authToken);

}


