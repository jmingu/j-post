package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardFindDto {

    private Long boardId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime boardDate;
}
