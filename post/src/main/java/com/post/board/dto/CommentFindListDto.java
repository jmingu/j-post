package com.post.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentFindListDto {
    private List<CommentFindDto> commentFindDtos;
    private Long totalComment;
}
