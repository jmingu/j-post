package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentEditDto {
    private Long commentId;
    private String loginId;
    private String content;
}
