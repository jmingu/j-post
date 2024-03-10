package com.post.board.repository;

import com.common.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentReposiroty extends JpaRepository<CommentEntity, Long> {


    /**
     * 댓글 조회
     */
    @Query("select commnt from CommentEntity commnt where commnt.commentId = :commentId and commnt.deleteDate is null")
    CommentEntity findCommnt(@Param("commentId") Long commentId);

    /**
     * 댓글 리스트 조회
     */
    @Query("select commnt " +
            "from CommentEntity commnt " +
            "where commnt.boardEntity.boardId = :boardId " +
            "and commnt.deleteDate is null " +
            "and commnt.parentCommentId is null"
    )
    Page<CommentEntity> findCommntList(@Param("boardId") Long boardId, Pageable pageable);

    /**
     * 대댓글 리스트 조회
     */
    @Query("select commnt " +
            "from CommentEntity commnt " +
            "where commnt.boardEntity.boardId = :boardId " +
            "and commnt.deleteDate is null " +
            "and commnt.parentCommentId = :parentCommentId"
    )
    Page<CommentEntity> findReCommntList(@Param("boardId") Long boardId, @Param("parentCommentId") Long parentCommentId, Pageable pageable);

    /**
     * 댓글 수정
     */
    @Modifying
    @Query("update CommentEntity commnt set commnt.content = :content, commnt.updateDate = :updateDate, commnt.updateBy = :updateBy where commnt.commentId = :commentId")
    int updateCommnt(@Param("commentId") Long commentId, @Param("content") String content, @Param("updateDate") LocalDateTime updateDate, @Param("updateBy") String updateBy);

    /**
     * 댓글 삭제
     */
    @Modifying
    @Query("update CommentEntity commnt set commnt.deleteDate = :deleteDate, commnt.deleteBy = :deleteBy where commnt.commentId = :commentId")
    int deleteCommnt(@Param("commentId") Long commentId, @Param("deleteDate") LocalDateTime deleteDate, @Param("deleteBy") String deleteBy);
}
