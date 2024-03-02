package com.post.board.service;

import com.common.entity.*;
import com.common.exception.JApplicationException;
import com.post.board.dto.*;
import com.post.board.repository.CommentReactionRepositoty;
import com.post.board.repository.CommentReposiroty;
import com.post.common.configuration.util.CryptoUtil;
import com.post.common.feign.UserInfoFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final UserInfoFeignClient userInfoFeignClient;

    private final CommentReposiroty commentReposiroty;

    private final CommentReactionRepositoty commentReactionRepositoty;

    /**
     * 댓글, 대댓글 등록
     */
    @Transactional
    public void createComment(CommentCreateDto commentCreateDto, String header) {

        BoardEntity boardEntity = new BoardEntity(commentCreateDto.getBoardId());

        // 로그인 아이디로 user_id 찾기
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, commentCreateDto.getLoginId());

        // 댓글 등록 (대댓글 아님)
        if (commentCreateDto.getParentCommentId() == null) {
            CommentEntity commentEntity = new CommentEntity(boardEntity, userResult.getBody().getResult().getUserId(), commentCreateDto.getContent());

            commentReposiroty.save(commentEntity);
        }
        // 대댓글일 등록
        else {
            CommentEntity commnt = commentReposiroty.findCommnt(commentCreateDto.getParentCommentId());

            // 부모댓글이 없을경우
            if (commnt == null) {
                throw new JApplicationException("댓글이 없습니다.");
            }

            CommentEntity commentEntity = new CommentEntity(boardEntity, userResult.getBody().getResult().getUserId(), commentCreateDto.getContent(), commentCreateDto.getParentCommentId());

            commentReposiroty.save(commentEntity);
        }

    }

    /**
     * 댓글 조회
     */
    public List<CommentFindDto> findComment(Long boardId, Pageable pageable, String header) {

        List<CommentEntity> commntList = commentReposiroty.findCommntList(boardId, pageable);

        List<CommentFindDto> commentFindDtos = new ArrayList<>();
        for (CommentEntity commentEntity : commntList) {
            // todo 리스트를 한번에 가져올 수 있는 방안 찾기
            ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, commentEntity.getUserId());
            log.debug("commentEntity ==> {}", commentEntity.getContent());

            CommentFindDto commentFindDto = CommentFindDto.builder()
                    .commentId(commentEntity.getCommentId())
                    .content(commentEntity.getContent())
                    .nickname(userResult.getBody().getResult().getNickname())
                    .createDate(commentEntity.getCreateDate())
                    .build();

            commentFindDtos.add(commentFindDto);

        }

        return commentFindDtos;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void editComment(CommentEditDto commentEditDto, String header) {

        // 댓글 조회
        CommentEntity commnt = commentReposiroty.findCommnt(commentEditDto.getCommentId());

        // 로그인 아이디로 user_id 찾기
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, commentEditDto.getLoginId());

        // 작성자인지 검증
        if (!commnt.getUserId().equals(userResult.getBody().getResult().getUserId())) {
            throw new JApplicationException("작성자만 수정할 수 있습니다.");
        }

        commentReposiroty.updateCommnt(commentEditDto.getCommentId(), commentEditDto.getContent(), LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());

    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId, String loginId, String header) {

        // 댓글 조회
        CommentEntity commnt = commentReposiroty.findCommnt(commentId);

        // 로그인 아이디로 user_id 찾기
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, loginId);

        // 작성자인지 검증
        if (!commnt.getUserId().equals(userResult.getBody().getResult().getUserId())) {
            throw new JApplicationException("작성자만 삭제할 수 있습니다.");
        }

        commentReposiroty.deleteCommnt(commentId, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());

    }

    /**
     * 좋아요
     */
    @Transactional
    public void commnetLike(Long commnetId, String header) throws Exception {

        CommentEntity commnt = commentReposiroty.findCommnt(commnetId);

        if (commnt == null) {
            throw new JApplicationException("없는 댓글입니다.");
        }

        // 헤더에 login_id 존재
        String loginId = CryptoUtil.decrypt(header);

        // 로그인 아이디로 사용자 정보 조회
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, loginId);
        CommentEntity commnet = new CommentEntity(commnetId);

        CommentReactionEntity commentReaction = commentReactionRepositoty.findByCommentEntityAndUserId(commnet, userResult.getBody().getResult().getUserId());

        // null일경우 처음 좋아요, 좋아요 등록
        if (commentReaction == null) {

            CommentEntity commnetEntity = new CommentEntity(commnetId);
            // 1 좋아요, 2 싫어요, 3 취소
            ReactionEntity reactionEntity = new ReactionEntity(1);
            CommentReactionEntity commentReactionEntity = new CommentReactionEntity(commnetEntity, reactionEntity, userResult.getBody().getResult().getUserId());

            commentReactionRepositoty.save(commentReactionEntity);
        }
        // 1 이미 좋아요를 누르상태 : 취소로 상태 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 1) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 3, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        // 2 싫어요가 등록되어있는 상태 : 좋아요로 다시 상태 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 2) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 1, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        // 3 취소가 등록되어있는 상태 : 다시 좋아요로 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 3) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 1, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        else {
            throw new JApplicationException("잘못된 상태 값");
        }

    }

    /**
     * 싫어요
     */
    @Transactional
    public void commnetBad(Long commnetId, String header) throws Exception {

        CommentEntity findComment = commentReposiroty.findCommnt(commnetId);

        if (findComment == null) {
            throw new JApplicationException("없는 댓글입니다.");
        }

        // 헤더에 login_id 존재
        String loginId = CryptoUtil.decrypt(header);

        // 로그인 아이디로 사용자 정보 조회
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, loginId);
        CommentEntity commnet = new CommentEntity(commnetId);

        CommentReactionEntity commentReaction = commentReactionRepositoty.findByCommentEntityAndUserId(commnet, userResult.getBody().getResult().getUserId());

        // null일경우 처음 싫어요, 좋아요 등록
        if (commentReaction == null) {

            CommentEntity commnetEntity = new CommentEntity(commnetId);
            // 1 좋아요, 2 싫어요, 3 취소
            ReactionEntity reactionEntity = new ReactionEntity(2);
            CommentReactionEntity commentReactionEntity = new CommentReactionEntity(commnetEntity, reactionEntity, userResult.getBody().getResult().getUserId());

            commentReactionRepositoty.save(commentReactionEntity);
        }
        // 2 이미 싫어요를 누르상태 : 취소로 상태 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 2) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 3, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        // 1 좋아요가 등록되어있는 상태 : 싫어요로 다시 상태 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 1) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 2, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        // 3 취소가 등록되어있는 상태 : 다시 싫어요로 변경
        else if (commentReaction.getReactionEntity().getReactionId() == 3) {
            commentReactionRepositoty.updateReaction(commentReaction.getCommentReactionId(), userResult.getBody().getResult().getUserId(), 2, LocalDateTime.now(), "user_id : " + userResult.getBody().getResult().getUserId());
        }
        else {
            throw new JApplicationException("잘못된 상태 값");
        }

    }
}
