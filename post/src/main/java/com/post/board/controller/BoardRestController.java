package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.post.board.dto.BoardCreateDto;
import com.post.board.dto.BoardEditDto;
import com.post.board.dto.BoardFindDto;
import com.post.board.dto.BoardFindListDto;
import com.post.board.dto.request.BoardRequestDto;
import com.post.board.dto.response.BoardFindResponseDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .userId(principal.getName() == null ? null : Long.parseLong(principal.getName()))
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

        Pageable pageable = PageRequest.of((page-1), 10, Sort.by(Sort.Direction.DESC, "boardId"));

        BoardFindListDto board = boardService.findBoard(pageable, header);

        return CommonResponseDto.success(
                BoardListResponseDto.builder()
                        .boardList(board.getBoardFindDtos())
                        .totalBoard(board.getTotalBoard())
                        .build()
        );
    }

    /**
     * 상세조회
     */
    @GetMapping("/borads/{boardId}")
    public ResponseEntity<CommonResponseDto> findByBoardId(@PathVariable Long boardId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        BoardFindDto boardDetail = boardService.findByBoardId(boardId, header);

        BoardFindResponseDto boardFindResponseDto = BoardFindResponseDto.builder()
                .boardId(boardDetail.getBoardId())
                .nickname(boardDetail.getNickname())
                .title(boardDetail.getTitle())
                .content(boardDetail.getContent())
                .viewCount(boardDetail.getViewCount())
                .createDate(boardDetail.getCreateDate())
                .editEnable(boardDetail.getEditEnable())
                .likeCount(boardDetail.getLikeCount())
                .badCount(boardDetail.getBadCount())
                .likeClick(boardDetail.getLikeClick())
                .badClick(boardDetail.getBadClick())
                .build();

        return CommonResponseDto.success(boardFindResponseDto);
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
        Map resultMap = new HashMap<>();
        resultMap.put("boardId", boardId);

        return CommonResponseDto.success(resultMap);


    }

    /**
     * 좋아요
     */
    @PostMapping("/borads/{boardId}/like")
    public ResponseEntity<CommonResponseDto> boardLike(@PathVariable Long boardId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        boardService.boardLike(boardId, header);

        BoardFindDto boardDetail = boardService.findByBoardId(boardId, header);

        BoardFindResponseDto boardFindResponseDto = BoardFindResponseDto.builder()
                .boardId(boardDetail.getBoardId())
                .nickname(boardDetail.getNickname())
                .title(boardDetail.getTitle())
                .content(boardDetail.getContent())
                .viewCount(boardDetail.getViewCount())
                .createDate(boardDetail.getCreateDate())
                .editEnable(boardDetail.getEditEnable())
                .likeCount(boardDetail.getLikeCount())
                .badCount(boardDetail.getBadCount())
                .likeClick(boardDetail.getLikeClick())
                .badClick(boardDetail.getBadClick())
                .build();

        Map resultMap = new HashMap<>();
        resultMap.put("boardDetail", boardDetail);

        return CommonResponseDto.success(resultMap);

    }

    /**
     * 싫어요
     */
    @PostMapping("/borads/{boardId}/bad")
    public ResponseEntity<CommonResponseDto> boardBad(@PathVariable Long boardId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        boardService.boardBad(boardId, header);

        BoardFindDto boardDetail = boardService.findByBoardId(boardId, header);

        BoardFindResponseDto boardFindResponseDto = BoardFindResponseDto.builder()
                .boardId(boardDetail.getBoardId())
                .nickname(boardDetail.getNickname())
                .title(boardDetail.getTitle())
                .content(boardDetail.getContent())
                .viewCount(boardDetail.getViewCount())
                .createDate(boardDetail.getCreateDate())
                .editEnable(boardDetail.getEditEnable())
                .likeCount(boardDetail.getLikeCount())
                .badCount(boardDetail.getBadCount())
                .likeClick(boardDetail.getLikeClick())
                .badClick(boardDetail.getBadClick())
                .build();

        Map resultMap = new HashMap<>();
        resultMap.put("boardDetail", boardDetail);

        return CommonResponseDto.success(resultMap);
    }
}
