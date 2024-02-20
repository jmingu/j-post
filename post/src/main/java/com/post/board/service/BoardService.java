package com.post.board.service;

import com.common.entity.PostEntity;
import com.post.board.dto.PostDto;
import com.post.board.dto.UserResultDto;
import com.post.board.repository.BoardReposiroty;
import com.post.common.feign.UserInfoFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardReposiroty boardReposiroty;
    private final UserInfoFeignClient userInfoFeignClient;
    public void createPost(PostDto postDto, String header) {

        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, postDto.getLoginId());

        log.debug("getUserId====>{}", userResult.getBody().getResult().getUserId());
        PostEntity postEntity = new PostEntity(postDto.getTitle(), postDto.getContent(), userResult.getBody().getResult().getUserId());
        boardReposiroty.save(postEntity);

    }

}
