package com.post.sns.repository;

import com.post.sns.entity.Oauth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<Oauth2UserEntity, Integer> {

//    Optional<UserEntity> findByUserName(String userName);

     /**
      * 아이디 검증
      */
     long countByLoginId(String userName);

     /**
      * 회원정보 조회
      */
     Optional<Oauth2UserEntity> findByLoginId(String loginId);
//    Optional<OrgEntity> findByOrgId(long orgId);
}
