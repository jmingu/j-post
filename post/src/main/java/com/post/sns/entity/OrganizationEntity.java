package com.post.sns.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tb_organization")
public class OrganizationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long organizationId;

    private String organizationName;

    private LocalDateTime organizationStartDate;

    private LocalDateTime organizationEndDate;

    @OneToMany(mappedBy = "organizationEntity")
    private List<Oauth2UserEntity> oauth2UserEntity;
}
