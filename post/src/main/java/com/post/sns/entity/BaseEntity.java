package com.post.sns.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    private String createBy;

    @LastModifiedDate
    private LocalDateTime updateDate;

    private String updateBy;

    private LocalDateTime deleteDate;

    private String deleteBy;




}
