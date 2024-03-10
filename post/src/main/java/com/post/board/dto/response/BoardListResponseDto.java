package com.post.board.dto.response;

import com.post.board.dto.BoardFindDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardListResponseDto {
    private List<BoardFindDto> boardList;
    private long totalBoard;
}
