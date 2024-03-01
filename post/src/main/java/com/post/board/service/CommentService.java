package com.post.board.service;

import com.common.entity.BoardEntity;
import com.common.entity.CommentEntity;
import com.post.board.dto.CommentCreateDto;
import com.post.board.dto.UserResultDto;
import com.post.board.repository.CommentReposiroty;
import com.post.common.feign.UserInfoFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final UserInfoFeignClient userInfoFeignClient;

    private final CommentReposiroty commentReposiroty;

    /**
     * 댓글 등록
     */
    @Transactional
    public void createComment(CommentCreateDto commentCreateDto, String header) {

        BoardEntity boardEntity = new BoardEntity(commentCreateDto.getBoardId());

        // 로그인 아이디로 user_id 찾기
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, commentCreateDto.getLoginId());

        CommentEntity commentEntity = new CommentEntity(boardEntity, userResult.getBody().getResult().getUserId(), commentCreateDto.getContent(), commentCreateDto.getParentCommentId());

        commentReposiroty.save(commentEntity);

    }

}
