package com.post.board.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BoardFindResponseDto {

    private Long boardId;
    private String nickname;
    private String title;
    private String content;
    private long viewCount;
    private String createDate;
    private Boolean editEnable;
}
