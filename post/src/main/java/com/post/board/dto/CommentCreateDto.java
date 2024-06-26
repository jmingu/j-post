package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentCreateDto {
    private Long boardId;
    private Long parentCommentId;
    private String loginId;
    private String content;
}
