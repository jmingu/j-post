package com.post.board.repository;

import com.common.entity.BoardEntity;
import com.common.entity.BoardReactionEntity;
import com.common.entity.CommentEntity;
import com.common.entity.CommentReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentReactionRepositoty extends JpaRepository<CommentReactionEntity, Long> {


    /**
     * 리액션 조회
     */
    CommentReactionEntity findByCommentEntityAndUserId(@Param("commentEntity") CommentEntity commentEntity, @Param("userId")Long userId);

    /**
     * 리액션 업데이트
     */
    @Modifying
    @Query("update CommentReactionEntity reaction " +
            "set reaction.reactionEntity.reactionId = :reactionId, reaction.updateDate = :updateDate, reaction.updateBy = :updateBy " +
            "where reaction.commentReactionId = :commentReactionId and reaction.userId = :userId"
    )
    int updateReaction(@Param("commentReactionId") Long commentReactionId, @Param("userId") Long userId, @Param("reactionId") Integer reactionId, @Param("updateDate") LocalDateTime updateDate, @Param("updateBy") String updateBy);
}
