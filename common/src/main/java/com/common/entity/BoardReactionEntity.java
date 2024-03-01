package com.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_board_reaction")
public class BoardReactionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardReactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_id")
    private ReactionEntity reactionEntity;
    private Long userId;

    public BoardReactionEntity(BoardEntity boardEntity, ReactionEntity reactionEntity, Long userId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("user_id : " + userId);
        this.boardEntity = boardEntity;
        this.reactionEntity = reactionEntity;
        this.userId = userId;
    }
}
