package com.post.board.repository;

import com.common.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReposiroty extends JpaRepository<PostEntity, Long> {

}
