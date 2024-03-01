package com.post.board.controller;


import com.common.dto.CommonResponseDto;
import com.post.board.dto.CommentCreateDto;
import com.post.board.dto.request.CommentRequestDto;
import com.post.board.service.CommentService;
import com.post.common.configuration.util.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {
    private final CommentService commentService;

    /**
     * 등록
     */
    @PostMapping("/comment/{boardId}")
    public ResponseEntity<CommonResponseDto> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) throws Exception {

        // 헤더 정보
        final String header = request.getHeader("X-Auth-Status");

        String loginId = CryptoUtil.decrypt(header);

        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .boardId(boardId)
                .loginId(loginId)
                .content(commentRequestDto.getContent())
                .parentCommentId(commentRequestDto.getParentCommentId())
                .build();

        commentService.createComment(commentCreateDto, header);

        return CommonResponseDto.success(commentRequestDto);
    }


}
