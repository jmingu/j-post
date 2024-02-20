package com.post.common.feign;

import com.post.board.dto.UserResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user", url = "${service.j-user-service}")
public interface UserInfoFeignClient {
    @GetMapping("/api/user/{loginId}")
    ResponseEntity<UserResultDto> getUserResult(@RequestHeader("Authorization") String token, @PathVariable String loginId);

}


