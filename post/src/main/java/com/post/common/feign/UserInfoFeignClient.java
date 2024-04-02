package com.post.common.feign;

import com.common.dto.CommonResponseDto;
import com.post.board.dto.UserDto;
import com.post.board.dto.UserListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user", url = "${service.j-user-service}")
public interface UserInfoFeignClient {

    /**
     * 회원정보 조회(login_id)
     */
    @GetMapping("/api/user/login")
    ResponseEntity<CommonResponseDto<UserDto>> getUserLoginResult(@RequestHeader("X-Auth-Status") String token, @RequestParam String loginId);

    /**
     * 회원정보 조회(user_id)
     */
    @GetMapping("/api/user")
    ResponseEntity<CommonResponseDto<UserDto>> getUserResult(@RequestHeader("X-Auth-Status") String token, @RequestParam Long userId);

    /**
     * 회원정보 조회(user_id)
     */
    @GetMapping("/api/user/list")
    ResponseEntity<CommonResponseDto<UserListDto>> getUserList(@RequestHeader("X-Auth-Status") String token, @RequestParam List<Long> userIdList);


}


