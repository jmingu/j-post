package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardCreateDto {
    private Long userId;
    private String title;
    private String content;
}
