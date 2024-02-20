package com.common.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name="tb_post")
public class PostEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private LocalDateTime postDate;

    private String title;

    private String content;

    private Long userId;

    public PostEntity(String title, String content, Long userId) {
        setCreateDate(LocalDateTime.now());
        setCreateBy("SYSTEM");
        this.postDate = LocalDateTime.now();
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
