package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RepliesCreateDto {
    private Long commnetId;
    private String loginId;
    private String content;
}
