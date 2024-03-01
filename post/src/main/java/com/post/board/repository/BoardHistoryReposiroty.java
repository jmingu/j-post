package com.post.board.repository;

import com.common.entity.BoardEntity;
import com.common.entity.BoardHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BoardHistoryReposiroty extends JpaRepository<BoardHistoryEntity, Long> {

    /**
     * 게시물 조회 이력 조회
     */
    @Query("select count(history) from BoardHistoryEntity history where history.boardEntity.boardId = :boardId and history.userId = :userId")
    int countByBoardIdAndUserId(@Param("boardId")Long boardId,  @Param("userId")Long userId);


}
