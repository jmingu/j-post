package com.post.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BoardLikeBadCountDto {
    private long boardId;
    private long count;
}
