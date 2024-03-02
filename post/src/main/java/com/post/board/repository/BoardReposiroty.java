package com.post.board.repository;

import com.common.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BoardReposiroty extends JpaRepository<BoardEntity, Long> {
    /**
     * 리스트 조회
     */
    @Query("select board from BoardEntity as board where board.deleteDate is null ")
    Page<BoardEntity> findBoardList(Pageable pageable);

    /**
     * 상세조회
     */
    @Query("select board from BoardEntity as board where board.boardId = :boardId and board.deleteDate is null ")
    BoardEntity findByBoardId(@Param("boardId") Long boardId);

    /**
     * 수정
     */
    @Modifying // select 문이 아님을 나타낸다
    @Query("update BoardEntity as board set board.title = :title, board.content = :content, board.updateBy = :userId, board.updateDate = :updateDate where board.boardId = :boardId")
    int editBoard(@Param("boardId") Long boardId,
                          @Param("title") String title,
                          @Param("content") String content,
                          @Param("userId") String userId,
                          @Param("updateDate") LocalDateTime updateDate);

    /**
     * 삭제
     */
    @Modifying // select 문이 아님을 나타낸다
    @Query("update BoardEntity as board set board.deleteBy = :userId, board.deleteDate = :deleteDate where board.boardId = :boardId")
    int deleteBoard(@Param("boardId") Long boardId,
                  @Param("userId") String userId,
                  @Param("deleteDate") LocalDateTime deleteDate);

    /**
     * 조회수 +1
     */
    @Modifying
    @Query("update BoardEntity board set board.viewCnt = board.viewCnt +1 where board.boardId = :boardId")
    int updateViewCount(@Param("boardId") Long boardId);


}
