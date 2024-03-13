package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BoardFindDto {

    private Long boardId;
    private String nickname;
    private String title;
    private String content;
    private long viewCount;
    private String createDate;
    private Boolean editEnable;
    private long likeCount;
    private long badCount;
    private Boolean likeClick;
    private Boolean badClick;
}
