package com.post.board.dto.response;

import com.post.board.dto.CommentFindDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentListResponseDto {
    private List<CommentFindDto> commentList;
    private long totalComment;
}
