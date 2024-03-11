package com.post.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CommentLikeBadCountDto {
    private long commentId;
    private long count;
}
