package com.post.board.service;

import com.common.entity.BoardEntity;
import com.common.exception.JApplicationException;
import com.post.board.dto.BoardCreateDto;
import com.post.board.dto.BoardEditDto;
import com.post.board.dto.BoardFindDto;
import com.post.board.dto.UserResultDto;
import com.post.board.repository.BoardReposiroty;
import com.post.common.configuration.util.CryptoUtil;
import com.post.common.feign.UserInfoFeignClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class BoardService {
    private final BoardReposiroty boardReposiroty;
    private final UserInfoFeignClient userInfoFeignClient;

    /**
     * 등록
     */
    @Transactional
    public void createBoard(BoardCreateDto boardCreateDto, String header) {

        // 로그인 아이디로 user_id 찾기
        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserLoginResult(header, boardCreateDto.getLoginId());

        BoardEntity postEntity = new BoardEntity(boardCreateDto.getTitle(), boardCreateDto.getContent(), userResult.getBody().getResult().getUserId());

        // 저장
        boardReposiroty.save(postEntity);

    }

    /**
     * 리스트 조회
     */
    public List<BoardFindDto> findBoard(Pageable pageable, String header) {

        Page<BoardEntity> boardEntity = boardReposiroty.findBoardList(pageable);

        List<BoardFindDto> boardFindDtos = new ArrayList<>();

        for (int i = 0; i < boardEntity.getContent().size(); i++) {
            // todo 리스트를 한번에 가져올 수 있는 방안 찾기
            ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getContent().get(i).getUserId());

            // 엔티티를 바로 반환시키면 안좋음
            BoardFindDto boardFindDto = BoardFindDto.builder()
                    .boardId(boardEntity.getContent().get(i).getBoardId())
                    .title(boardEntity.getContent().get(i).getTitle())
                    .content(boardEntity.getContent().get(i).getContent())
                    .boardDate(boardEntity.getContent().get(i).getBoardDate())
                    .nickname(userResult.getBody().getResult().getNickname())
                    .build();

            boardFindDtos.add(boardFindDto);

        }

        return boardFindDtos;
    }

    /**
     * 상세 조회
     */
    public BoardFindDto findByBoardId(Long boardId, String header) {

        BoardEntity boardEntity = boardReposiroty.findByBoardId(boardId);

        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getUserId());

        BoardFindDto boardFindDto = BoardFindDto.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .boardDate(boardEntity.getBoardDate())
                .nickname(userResult.getBody().getResult().getNickname())
                .build();

        return boardFindDto;
    }

    /**
     * 수정
     */
    @Transactional
    public void editBoard(Long boardId, BoardEditDto boardEditDto, String header) throws Exception {

        BoardEntity boardEntity = boardReposiroty.findByBoardId(boardId);

        // 헤더에 login_id 존재
        String loginId = CryptoUtil.decrypt(header);

        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getUserId());

        // 작성자인지 검증
        if (!loginId.equals(userResult.getBody().getResult().getLoginId())) {
            throw new JApplicationException("작성자만 수정할 수 있습니다.");
        }

        // 수정
        int updateInt = boardReposiroty.editBoard(boardId, boardEditDto.getTitle(), boardEditDto.getContent(), "user_id : " + userResult.getBody().getResult().getUserId(), LocalDateTime.now());

        // 수정된 게시물이 없을 시
        if (updateInt == 0) {
            throw new JApplicationException("수정된 게시물이 없습니다.");
        }

    }

    /**
     * 삭제
     */
    @Transactional
    public void deleteBoard(Long boardId, String header) throws Exception {

        BoardEntity boardEntity = boardReposiroty.findByBoardId(boardId);

        // 헤더에 login_id 존재
        String loginId = CryptoUtil.decrypt(header);

        ResponseEntity<UserResultDto> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getUserId());

        // 작성자인지 검증
        if (!loginId.equals(userResult.getBody().getResult().getLoginId())) {
            throw new JApplicationException("작성자만 삭제할 수 있습니다.");
        }

        // 삭제
        int updateInt = boardReposiroty.deleteBoard(boardId, "user_id : " + userResult.getBody().getResult().getUserId(), LocalDateTime.now());

        // 삭제된 게시물이 없을 시
        if (updateInt == 0) {
            throw new JApplicationException("삭제된 게시물이 없습니다.");
        }

    }



}
