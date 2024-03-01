package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentFindDto {
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createDate;

}
