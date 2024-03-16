package com.post.board.service;

import com.common.dto.CommonResponseDto;
import com.common.entity.BoardEntity;
import com.common.entity.BoardHistoryEntity;
import com.common.entity.BoardReactionEntity;
import com.common.entity.ReactionEntity;
import com.common.exception.JApplicationException;
import com.post.board.dto.*;
import com.post.board.repository.BoardHistoryReposiroty;
import com.post.board.repository.BoardReactionRepositoty;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardReposiroty boardReposiroty;

    private final BoardReactionRepositoty boardReactionRepositoty;
    private final UserInfoFeignClient userInfoFeignClient;
    private final BoardHistoryReposiroty boardHistoryReposiroty;

    /**
     * 등록
     */
    @Transactional
    public void createBoard(BoardCreateDto boardCreateDto, String header) throws Exception {

        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));
        if (userId < 0) {
            throw new JApplicationException("로그인이 필요합니다.");
        }

        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, userId);

        if (userResult.getBody().getResult().getNickname() == null) {
            throw new JApplicationException("닉네임이 등록이 필요합니다.");
        }

        BoardEntity postEntity = new BoardEntity(boardCreateDto.getTitle(), boardCreateDto.getContent(), userId);

        // 저장
        boardReposiroty.save(postEntity);

    }

    /**
     * 리스트 조회
     */
    public BoardFindListDto findBoard(Pageable pageable, String header) {

        Page<BoardEntity> boardEntitys = boardReposiroty.findBoardList(pageable);

        List<BoardFindDto> boardFindDtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        for (BoardEntity boardEntity : boardEntitys.getContent()) {
            // todo 리스트를 한번에 가져올 수 있는 방안 찾기
            ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getUserId());

            // 엔티티를 바로 반환시키면 안좋음
            BoardFindDto boardFindDto = BoardFindDto.builder()
                    .boardId(boardEntity.getBoardId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .nickname(userResult.getBody().getResult().getNickname())
                    .viewCount(boardEntity.getViewCnt() == null ? 0 : boardEntity.getViewCnt())
                    .createDate(boardEntity.getCreateDate().format(formatter))
                    .build();

            boardFindDtos.add(boardFindDto);
        }

        BoardFindListDto boardFindListDto = BoardFindListDto.builder()
                .totalBoard(boardEntitys.getTotalElements())
                .boardFindDtos(boardFindDtos)
                .build();

        return boardFindListDto;
    }

    /**
     * 상세 조회
     */
    @Transactional
    public BoardFindDto findByBoardId(Long boardId, String header) throws Exception {
        // userId가 음수면 로그인 안한 사용자
        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));

        // 조회수 +1
        try {
            boardReposiroty.updateViewCount(boardId);
        } catch (Exception e) {
            log.error("게시물 조회수 에러 ==> {}", e.getMessage());
        }

        // 게시물 조회
        BoardEntity boardEntity = boardReposiroty.findByBoardId(boardId);

        List<Long> boardIdList = new ArrayList<>();
        boardIdList.add(boardEntity.getBoardId());

        // 좋아요
        List<BoardLikeBadCountDto> boardLike = boardReactionRepositoty.findBoardLikeBadCount(boardIdList, 1);
        log.debug("boardLike ==> {}", boardLike);
        // 싫어요
        List<BoardLikeBadCountDto> boardBad = boardReactionRepositoty.findBoardLikeBadCount(boardIdList, 2);
        log.debug("boardBad ==> {}", boardBad);

        // 좋아요 / 싫어요 클릭여부
        List<BoardReactionEntity> boardLikeBadClick = boardReactionRepositoty.findBoardLikeBadClick(boardIdList, userId);

        // 사용자 조회
        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, boardEntity.getUserId());


        // 조회이력 등록, 조회이력은 게시물당 1번
        if (userId > 0) {
            try {
                int historyCount = boardHistoryReposiroty.countByBoardIdAndUserId(boardId, userId);

                // 조회 이력이 없으면 등록
                if (historyCount == 0) {
                    BoardHistoryEntity boardHistoryEntity = new BoardHistoryEntity(boardEntity, userId);
                    boardHistoryReposiroty.save(boardHistoryEntity);
                }
                // todo 업데이트 이력이 필요 시 개발

            } catch (Exception e) {
                log.error("조회 이력 에러 ==> {}", e.getMessage());
            }
        }

        Boolean likeClick = null;
        Boolean badClick = null;
        // 좋아요 / 싫어요 클릭여부
        for (BoardReactionEntity boardReactionEntity : boardLikeBadClick) {
            if (boardEntity.getBoardId() == boardReactionEntity.getBoardEntity().getBoardId()) {
                // 좋아요
                if (boardReactionEntity.getReactionEntity().getReactionId() == 1) {
                    likeClick = true;
                } else if (boardReactionEntity.getReactionEntity().getReactionId() == 2) {
                    badClick = true;
                } else {
                    likeClick = false;
                    badClick = false;
                }
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        BoardFindDto boardFindDto = BoardFindDto.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .nickname(userResult.getBody().getResult().getNickname())
                .viewCount(boardEntity.getViewCnt() == null ? 0 : boardEntity.getViewCnt())
                .createDate(boardEntity.getCreateDate().format(formatter))
                .editEnable(boardEntity.getUserId() == userId ? true : false)
                .likeCount(boardLike.size() == 0 ? 0 : boardLike.get(0).getCount())
                .badCount(boardBad.size() == 0 ? 0 : boardBad.get(0).getCount())
                .likeClick(likeClick)
                .badClick(badClick)
                .build();

        return boardFindDto;
    }

    /**
     * 수정
     */
    @Transactional
    public void editBoard(Long boardId, BoardEditDto boardEditDto, String header) throws Exception {

        BoardEntity boardEntity = boardReposiroty.findByBoardId(boardId);

        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));
        if (userId < 0) {
            throw new JApplicationException("로그인이 필요합니다.");
        }

        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, userId);

        if (userResult.getBody().getResult().getNickname() == null) {
            throw new JApplicationException("닉네임이 등록이 필요합니다.");
        }


        // 작성자인지 검증
        if (userId != boardEntity.getUserId()) {
            throw new JApplicationException("작성자만 수정할 수 있습니다.");
        }

        // 수정
        int updateInt = boardReposiroty.editBoard(boardId, boardEditDto.getTitle(), boardEditDto.getContent(), "user_id : " + userId, LocalDateTime.now());

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

        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));
        if (userId < 0) {
            throw new JApplicationException("로그인이 필요합니다.");
        }

        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, userId);

        if (userResult.getBody().getResult().getNickname() == null) {
            throw new JApplicationException("닉네임이 등록이 필요합니다.");
        }

        // 작성자인지 검증
        if (userId != boardEntity.getUserId()) {
            throw new JApplicationException("작성자만 수정할 수 있습니다.");
        }

        // 삭제
        int updateInt = boardReposiroty.deleteBoard(boardId, "user_id : " + userId, LocalDateTime.now());

        // 삭제된 게시물이 없을 시
        if (updateInt == 0) {
            throw new JApplicationException("삭제된 게시물이 없습니다.");
        }

    }

    /**
     * 좋아요
     */
    @Transactional
    public void boardLike(Long boardId, String header) throws Exception {

        BoardEntity findBoard = boardReposiroty.findByBoardId(boardId);

        if (findBoard == null) {
            throw new JApplicationException("없는 게시글입니다.");
        }

        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));
        if (userId < 0) {
            throw new JApplicationException("로그인이 필요합니다.");
        }

        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, userId);

        if (userResult.getBody().getResult().getNickname() == null) {
            throw new JApplicationException("닉네임이 등록이 필요합니다.");
        }


        BoardEntity board = new BoardEntity(boardId);

        BoardReactionEntity boardReaction = boardReactionRepositoty.findByBoardEntityAndUserId(board, userId);

        // null일경우 처음 좋아요, 좋아요 등록
        if (boardReaction == null) {

            BoardEntity boardEntity = new BoardEntity(boardId);
            // 1 좋아요, 2 싫어요, 3 취소
            ReactionEntity reactionEntity = new ReactionEntity(1);
            BoardReactionEntity boardReactionEntity = new BoardReactionEntity(boardEntity, reactionEntity, userId);

            boardReactionRepositoty.save(boardReactionEntity);
        }
        // 1 이미 좋아요를 누르상태 : 취소로 상태 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 1) {
            boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(), userId, 3, LocalDateTime.now(), "user_id : " + userId);
        }
        // 2 싫어요가 등록되어있는 상태 : 좋아요로 다시 상태 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 2) {
            boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(),userId, 1, LocalDateTime.now(), "user_id : " + userId);
        }
        // 3 취소가 등록되어있는 상태 : 다시 좋아요로 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 3) {
            boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(), userId, 1, LocalDateTime.now(), "user_id : " + userId);
        }
        else {
            throw new JApplicationException("잘못된 상태 값");
        }

    }

    /**
     * 싫어요
     */
    @Transactional
    public void boardBad(Long boardId, String header) throws Exception {

        BoardEntity findBoard = boardReposiroty.findByBoardId(boardId);

        if (findBoard == null) {
            throw new JApplicationException("없는 게시글입니다.");
        }

        // 헤더에 user_id 존재
        long userId = Long.parseLong(CryptoUtil.decrypt(header));
        if (userId < 0) {
            throw new JApplicationException("로그인이 필요합니다.");
        }

        ResponseEntity<CommonResponseDto<UserDto>> userResult = userInfoFeignClient.getUserResult(header, userId);

        if (userResult.getBody().getResult().getNickname() == null) {
            throw new JApplicationException("닉네임이 등록이 필요합니다.");
        }

        BoardEntity board = new BoardEntity(boardId);

        BoardReactionEntity boardReaction = boardReactionRepositoty.findByBoardEntityAndUserId(board, userId);

        // null일경우 싫어요 등록
        if (boardReaction == null) {

            BoardEntity boardEntity = new BoardEntity(boardId);
            // 1 좋아요, 2 싫어요, 3 취소
            ReactionEntity reactionEntity = new ReactionEntity(2);
            BoardReactionEntity boardReactionEntity = new BoardReactionEntity(boardEntity, reactionEntity, userId);

            boardReactionRepositoty.save(boardReactionEntity);
        }
        // 1 이미 싫어요를 누르상태 : 취소로 상태 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 2) {
            int i = boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(), userId, 3, LocalDateTime.now(), "user_id : " + userId);
        }
        // 2 좋아요가 등록되어있는 상태 : 싫어요로 다시 상태 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 1) {
            int i = boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(), userId, 2, LocalDateTime.now(), "user_id : " + userId);
        }
        // 3 취소가 등록되어있는 상태 : 다시 싫어요로 변경
        else if (boardReaction.getReactionEntity().getReactionId() == 3) {
            boardReactionRepositoty.updateReaction(boardReaction.getBoardReactionId(), userId, 2, LocalDateTime.now(), "user_id : " + userId);
        }
        else {
            throw new JApplicationException("잘못된 상태 값");
        }

    }

}
