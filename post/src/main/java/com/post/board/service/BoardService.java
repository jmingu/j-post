package com.post.board.service;

import com.common.entity.BoardEntity;
import com.post.board.dto.BoardCreateDto;
import com.post.board.dto.BoardFindDto;
import com.post.board.dto.UserResultDto;
import com.post.board.repository.BoardReposiroty;
import com.post.common.feign.UserInfoFeignClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardReposiroty boardReposiroty;
    private final UserInfoFeignClient userInfoFeignClient;

    /**
     * 등록
     */
    @Transactional
    public void createBoard(BoardCreateDto boardCreateDto, String header) {

        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, boardCreateDto.getLoginId());

        log.debug("getUserId====>{}", userResult.getBody().getResult().getUserId());
        BoardEntity postEntity = new BoardEntity(boardCreateDto.getTitle(), boardCreateDto.getContent(), userResult.getBody().getResult().getUserId());
        boardReposiroty.save(postEntity);

    }

    /**
     * 조회
     */

    public List<BoardFindDto> findBoard(Pageable pageable) {
        Page<BoardEntity> boardEntity = boardReposiroty.findAll(pageable);
        log.debug("boardEntity ==> {}", boardEntity.getContent().get(1));
        List<BoardFindDto> boardFindDtos = new ArrayList<>();
        for (int i = 0; i < boardEntity.getContent().size(); i++) {
            BoardFindDto boardFindDto = BoardFindDto.builder()
                    .boardId(boardEntity.getContent().get(i).getBoardId())
                    .title(boardEntity.getContent().get(i).getTitle())
                    .content(boardEntity.getContent().get(i).getContent())
                    .boardDate(boardEntity.getContent().get(i).getBoardDate())
                    .userName("TESTS")
                    .build();

            boardFindDtos.add(boardFindDto);

        }

        return boardFindDtos;
    }

}
