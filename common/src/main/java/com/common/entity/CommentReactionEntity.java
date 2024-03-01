package com.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_comment_reaction")
public class CommentReactionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentReactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity commentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_id")
    private ReactionEntity reactionEntity;

    private Long userId;


    public CommentReactionEntity(CommentEntity commentEntity, ReactionEntity reactionEntity, Long userId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("SYSTEM");
        this.commentEntity = commentEntity;
        this.reactionEntity = reactionEntity;
        this.userId = userId;
    }
}
