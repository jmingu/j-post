package com.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_board")
public class BoardEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    private String content;

    private Long userId;

    private Long viewCnt;

    public BoardEntity(Long boardId) {
        this.boardId = boardId;
    }

    public BoardEntity(String title, String content, Long userId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("SYSTEM");
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.viewCnt = 0L;
    }
}
