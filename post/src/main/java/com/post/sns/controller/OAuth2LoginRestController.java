package com.post.sns.controller;

import com.post.common.dto.CommonResponseDto;
import com.post.sns.dto.response.LoginTokenDto;
import com.post.sns.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginRestController {
    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping("/oauth/naver/callback")
    public ResponseEntity<CommonResponseDto> NaverLogin(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception {

        if (code == null || state == null) {
            return null;
        }

        LoginTokenDto token = oAuth2LoginService.getToken(code, state);

        return CommonResponseDto.success(token);
    }
}
