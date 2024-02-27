package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.post.board.dto.BoardCreateDto;
import com.post.board.dto.BoardEditDto;
import com.post.board.dto.BoardFindDto;
import com.post.board.dto.request.BoardRequestDto;
import com.post.board.dto.response.BoardListResponseDto;
import com.post.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        final String header = request.getHeader("X-Auth-Status");

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
     * 리스트 조회
     */
    @GetMapping("/borads")
    public ResponseEntity<CommonResponseDto> findBoard(@RequestParam int page, HttpServletRequest request) {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardId"));
        List<BoardFindDto> boardList = boardService.findBoard(pageable, header);

        return CommonResponseDto.success(BoardListResponseDto.builder().boardList(boardList).build());
    }

    /**
     * 상세조회
     */
    @GetMapping("/borads/{boardId}")
    public ResponseEntity<CommonResponseDto> findByBoardId(@PathVariable Long boardId, HttpServletRequest request) {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        BoardFindDto boardDetail = boardService.findByBoardId(boardId, header);

        return CommonResponseDto.success(boardDetail);
    }

    /**
     * 수정
     */
    @PatchMapping("/borads/{boardId}")
    public ResponseEntity<CommonResponseDto> editBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request, Principal principal) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        log.debug("principal ==> {}", principal.getName());
        BoardEditDto boardEditDto = BoardEditDto.builder()
                .loginId(principal.getName())
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .build();

        boardService.editBoard(boardId, boardEditDto, header);

        return CommonResponseDto.success(boardRequestDto);
    }

    /**
     * 삭제
     */
    @DeleteMapping("/borads/{boardId}")
    public ResponseEntity<CommonResponseDto> deleteBoard(@PathVariable Long boardId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        boardService.deleteBoard(boardId, header);

        return CommonResponseDto.success(boardId);
    }
}
