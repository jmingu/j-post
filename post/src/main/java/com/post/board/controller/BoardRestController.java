package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.common.entity.BoardEntity;
import com.post.board.dto.BoardCreateDto;
import com.post.board.dto.BoardFindDto;
import com.post.board.dto.request.BoardRequestDto;
import com.post.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {
    private final BoardService boardService;

    /**
     * 등록
     */
    @PostMapping("/borads")
    public ResponseEntity<CommonResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, Principal principal, HttpServletRequest request) {

        // 헤더 정보
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 토큰에서 받아온 유저 아이디
        log.debug("principal ==> {}", principal.getName());
        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .loginId(principal.getName())
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .build();

        boardService.createBoard(boardCreateDto, header);
        return CommonResponseDto.success(boardRequestDto);
    }

    /**
     * 조회
     */
    @GetMapping("/borads")
    public ResponseEntity<CommonResponseDto> findBoard() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "boardId"));
        List<BoardFindDto> board = boardService.findBoard(pageable);
        return CommonResponseDto.success(board);
    }
}
