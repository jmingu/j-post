package com.post.board.service;

import com.common.entity.BoardEntity;
import com.common.entity.CommentEntity;
import com.common.exception.JApplicationException;
import com.post.board.dto.*;
import com.post.board.repository.CommentReposiroty;
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
}
