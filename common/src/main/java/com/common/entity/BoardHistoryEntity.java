package com.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_board_history")
public class BoardHistoryEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;
    private Long userId;

    private LocalDate viewYmd;

    public BoardHistoryEntity(BoardEntity boardEntity, Long userId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("user_id : " + userId);
        this.viewYmd = LocalDate.now();
        this.boardEntity = boardEntity;
        this.userId = userId;
    }
}
