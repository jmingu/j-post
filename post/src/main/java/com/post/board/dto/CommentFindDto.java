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
    private String createDate;
    private boolean editEnable;
    private long likeCount;
    private long badCount;
    private boolean likeClick;
    private boolean badClick;

}
