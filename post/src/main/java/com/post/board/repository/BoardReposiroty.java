package com.post.board.repository;

import com.common.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReposiroty extends JpaRepository<BoardEntity, Long> {

}
