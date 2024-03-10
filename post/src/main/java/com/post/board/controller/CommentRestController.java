package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.post.board.dto.*;
import com.post.board.dto.request.CommentRequestDto;
import com.post.board.dto.response.CommentListResponseDto;
import com.post.board.service.CommentService;
import com.post.common.configuration.util.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {
    private final CommentService commentService;

    /**
     * 댓글, 대댓글 등록
     */
    @PostMapping("/borads/{boardId}/comments")
    public ResponseEntity<CommonResponseDto> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        String loginId = CryptoUtil.decrypt(header);

        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .boardId(boardId)
                .parentCommentId(commentRequestDto.getParentCommentId())
                .loginId(loginId)
                .content(commentRequestDto.getContent())
                .build();

        commentService.createComment(commentCreateDto, header);

        return CommonResponseDto.success(commentRequestDto);
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/borads/{boardId}/comments")
    public ResponseEntity<CommonResponseDto> findComment(@PathVariable Long boardId, @RequestParam int page, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        Pageable pageable = PageRequest.of((page-1), 2, Sort.by(Sort.Direction.DESC, "commentId"));

        CommentFindListDto comment = commentService.findComment(boardId, pageable, header);

        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                .commentList(comment.getCommentFindDtos())
                .totalComment(comment.getTotalComment())
                .build();

        return CommonResponseDto.success(commentListResponseDto);
    }

    /**
     * 대댓글 조회
     */
    @GetMapping("/borads/{boardId}/comments/{commentId}")
    public ResponseEntity<CommonResponseDto> findReComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestParam int page, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        Pageable pageable = PageRequest.of((page-1), 2, Sort.by(Sort.Direction.DESC, "commentId"));

        CommentFindListDto comment = commentService.findReComment(boardId,commentId, pageable, header);

        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                .commentList(comment.getCommentFindDtos())
                .totalComment(comment.getTotalComment())
                .build();

        return CommonResponseDto.success(commentListResponseDto);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/comments/{commnetId}")
    public ResponseEntity<CommonResponseDto> editComment(@PathVariable Long commnetId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        String loginId = CryptoUtil.decrypt(header);

        CommentEditDto commentEditDto = CommentEditDto.builder()
                .commentId(commnetId)
                .loginId(loginId)
                .content(commentRequestDto.getContent())
                .build();

        commentService.editComment(commentEditDto, header);

        return CommonResponseDto.success(commentRequestDto);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comments/{commnetId}")
    public ResponseEntity<CommonResponseDto> deleteComment(@PathVariable Long commnetId, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        String loginId = CryptoUtil.decrypt(header);

        commentService.deleteComment(commnetId, loginId, header);

        Map resultMap = new HashMap<>();
        resultMap.put("commnetId", commnetId);

        return CommonResponseDto.success(resultMap);
    }

    /**
     * 좋아요
     */
    @PostMapping("/comments/{commnetId}/like")
    public ResponseEntity<CommonResponseDto> commentLike(@PathVariable Long commnetId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        commentService.commnetLike(commnetId, header);
        Map resultMap = new HashMap<>();
        resultMap.put("commnetId", commnetId);

        return CommonResponseDto.success(resultMap);
    }

    /**
     * 싫어요
     */
    @PostMapping("/comments/{commnetId}/bad")
    public ResponseEntity<CommonResponseDto> commentBad(@PathVariable Long commnetId, HttpServletRequest request) throws Exception {
        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        commentService.commnetBad(commnetId, header);

        Map resultMap = new HashMap<>();
        resultMap.put("commnetId", commnetId);

        return CommonResponseDto.success(resultMap);
    }


}
