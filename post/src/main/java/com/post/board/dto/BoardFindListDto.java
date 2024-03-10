package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardFindListDto {
    private List<BoardFindDto> boardFindDtos;
    private Long totalBoard;
}
