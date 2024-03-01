package com.post.board.repository;

import com.common.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReposiroty extends JpaRepository<CommentEntity, Long> {


}
