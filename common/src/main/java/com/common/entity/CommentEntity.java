package com.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_comment")
public class CommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    private Long userId;

    private String content;

    private Long parentCommentId;


    public CommentEntity(BoardEntity boardEntity, Long userId, String content, Long parentCommentId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("SYSTEM");
        this.userId = userId;
        this.boardEntity = boardEntity;
        this.content = content;
        this.parentCommentId = parentCommentId;


    }
}
