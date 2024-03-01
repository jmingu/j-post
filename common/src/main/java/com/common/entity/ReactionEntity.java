package com.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="tb_reaction")
public class ReactionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    private String reaction;

    public ReactionEntity(String reaction) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("SYSTEM");
        this.reaction = reaction;
    }
}
