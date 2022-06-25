package com.yapp.betree.controller;

import com.yapp.betree.annotation.LoginUser;
import com.yapp.betree.dto.LoginUserDto;
import com.yapp.betree.dto.request.MessageRequestDto;
import com.yapp.betree.dto.response.MessagePageResponseDto;
import com.yapp.betree.exception.BetreeException;
import com.yapp.betree.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static com.yapp.betree.exception.ErrorCode.*;

@Api
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 칭찬 메시지 생성 (물주기)
     *
     * @param loginUser
     * @param requestDto
     * @return
     */
    @ApiOperation(value = "물주기", notes = "칭찬 메시지 생성")
    @ApiResponses({
            @ApiResponse(code = 400, message = "[F002]해당 페이지에 나무가 존재하지 않습니다."),
            @ApiResponse(code = 404, message = "[U001]회원을 찾을 수 없습니다.\n" +
                    "[T001]나무가 존재하지 않습니다.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/messages")
    public ResponseEntity<Long> createMessage(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                              @Valid @RequestBody MessageRequestDto requestDto) {

        log.info("물주기 요청 내용 : {}", requestDto);

        //TODO 비로그인 처리

        Long messageId = messageService.createMessage(loginUser.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageId);
    }

    /**
     * 메세지 목록 조회
     * - treeId 입력시 폴더별 조회
     *
     * @param loginUser
     * @param page
     * @param treeId
     * @return
     */
    @ApiOperation(value = "메세지 목록 조회", notes = "유저의 메세지 목록 조회- treeId 입력시 폴더별 조회 / 없으면 전체 조회")
    @ApiResponses({
            @ApiResponse(code = 404, message = "[T001]나무가 존재하지 않습니다.\n" +
                    "[U005]회원을 찾을 수 없습니다.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/messages")
    public ResponseEntity<MessagePageResponseDto> getMessageList(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                                 @RequestParam int page,
                                                                 @RequestParam(required = false) Long treeId) {

        log.info("[userId] : {}", loginUser.getId());

        return ResponseEntity.ok(messageService.getMessageList(loginUser.getId(), page, treeId));
    }

    /**
     * 메세지 공개 여부 설정 (열매 맺기)
     *
     * @param loginUser
     * @param messageIdList 선택한 메세지 ID List
     */
    @ApiOperation(value = "열매 맺기", notes = "메세지 공개 여부 설정")
    @ApiResponses({
            @ApiResponse(code = 400, message = "[C001]Invalid input value (열매 선택 개수 오류- 8개 초과)")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/messages/opening")
    public ResponseEntity<Object> openingMessage(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                 @RequestParam List<Long> messageIdList) {

        log.info("[messageIdList] : {}", messageIdList);

        messageService.updateMessageOpening(loginUser.getId(), messageIdList);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 메세지 삭제
     *
     * @param loginUser
     * @param messageIds
     * @return
     */
    @ApiOperation(value = "메세지 삭제", notes = "선택한 메세지 삭제")
    @ApiResponses({
            @ApiResponse(code = 404, message = "[M001]메세지가 존재하지 않습니다.")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/messages")
    public ResponseEntity<Object> deleteMessages(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                 @RequestBody List<Long> messageIds) {

        log.info("[messageIdList] : {}", messageIds + ", [loginUser Id] : {}" + loginUser.getId());

        messageService.deleteMessages(loginUser.getId(), messageIds);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 메세지 이동
     *
     * @param loginUser
     * @param messageIds
     * @param treeId
     * @return
     */
    @ApiOperation(value = "메세지 이동", notes = "선택한 메세지 폴더 이동")
    @ApiResponses({
            @ApiResponse(code = 404, message = "[M001]메세지가 존재하지 않습니다.\n" +
                    "[T001]나무가 존재하지 않습니다.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/messages/folder")
    public ResponseEntity<Object> moveMessageFolder(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                    @RequestBody List<Long> messageIds,
                                                    @RequestParam Long treeId) {

        log.info("[messageIdList] : {}", messageIds + ", [treeId] : {}" + treeId);

        messageService.moveMessageFolder(loginUser.getId(), messageIds, treeId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 메세지 즐겨찾기 여부 변경
     *
     * @param loginUser
     * @param messageId
     * @return
     */
    @ApiOperation(value = "메세지 즐겨찾기", notes = "선택한 메세지 즐겨찾기 설정")
    @ApiResponses({
            @ApiResponse(code = 404, message = "[M001]메세지가 존재하지 않습니다.\n" +
                    "[T001]나무가 존재하지 않습니다.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/messages/favorite")
    public ResponseEntity<Object> updateFavoriteMessage(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                        @RequestParam Long messageId) {

        log.info("[messageIdList] : {}", messageId);

        messageService.updateFavoriteMessage(loginUser.getId(), messageId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 즐겨찾기한 메세지 목록 조회
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "즐겨찾기 메세지 목록", notes = "즐겨찾기한 메세지 목록 조회")
    @ApiResponses({
            @ApiResponse(code = 404, message = "[U005]회원을 찾을 수 없습니다.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/messages/favorite")
    public ResponseEntity<MessagePageResponseDto> favoriteMessage(@ApiIgnore @LoginUser LoginUserDto loginUser,
                                                                  @RequestParam int page) {

        return ResponseEntity.ok(messageService.getFavoriteMessage(loginUser.getId(), page));
    }
}
