package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardEditDto {
    private String loginId;
    private String title;
    private String content;
}
