package com.post.common.feign;

import com.post.board.dto.UserResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user", url = "${service.j-user-service}")
public interface UserInfoFeignClient {

    /**
     * 회원정보 조회(login_id)
     */
    @GetMapping("/api/user/login")
    ResponseEntity<UserResultDto> getUserLoginResult(@RequestHeader("X-Auth-Status") String token, @RequestParam String loginId);

    /**
     * 회원정보 조회(user_id)
     */
    @GetMapping("/api/user")
    ResponseEntity<UserResultDto> getUserResult(@RequestHeader("X-Auth-Status") String token, @RequestParam Long userId);


}


