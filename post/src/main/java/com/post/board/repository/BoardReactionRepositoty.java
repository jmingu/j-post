package com.post.board.repository;

import com.common.entity.BoardEntity;
import com.common.entity.BoardReactionEntity;
import com.post.board.dto.BoardLikeBadCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardReactionRepositoty extends JpaRepository<BoardReactionEntity, Long> {


    /**
     * 리액션 조회
     */
    BoardReactionEntity findByBoardEntityAndUserId(@Param("boardEntity") BoardEntity boardEntity, @Param("userId")Long userId);

    /**
     * 리액션 업데이트
     */
    @Modifying
    @Query("update BoardReactionEntity reaction " +
            "set reaction.reactionEntity.reactionId = :reactionId, reaction.updateDate = :updateDate, reaction.updateBy = :updateBy " +
            "where reaction.boardReactionId = :boardReactionId and reaction.userId = :userId"
    )
    int updateReaction(@Param("boardReactionId") Long boardReactionId, @Param("userId") Long userId, @Param("reactionId") Integer reactionId, @Param("updateDate") LocalDateTime updateDate, @Param("updateBy") String updateBy);

    /**
     * 좋아요 / 싫어요 갯수
     */
    @Query("select " +
            "new com.post.board.dto.BoardLikeBadCountDto(reaction.boardEntity.boardId, count(reaction)) " +
            "from BoardReactionEntity reaction " +
            "where reaction.boardEntity.boardId in :boardIdList " +
            "and reaction.reactionEntity.reactionId = :reactionId " +
            "group by reaction.boardEntity.boardId"
    )
    List<BoardLikeBadCountDto> findBoardLikeBadCount(@Param("boardIdList") List<Long> boardIdList, long reactionId);
}
