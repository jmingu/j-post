package com.post.board.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long parentCommentId;
}
