package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.post.board.dto.PostDto;
import com.post.board.dto.request.PostRequestDto;
import com.post.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {
    private final BoardService boardService;

    @PostMapping("/posts")
    public ResponseEntity<CommonResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, Principal principal, HttpServletRequest request) {

        // 헤더 정보
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 토큰에서 받아온 유저 아이디
        log.debug("principal ==> {}", principal.getName());
        PostDto postDto = PostDto.builder()
                .loginId(principal.getName())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();

        boardService.createPost(postDto, header);



        return CommonResponseDto.success(postRequestDto);
    }
}
