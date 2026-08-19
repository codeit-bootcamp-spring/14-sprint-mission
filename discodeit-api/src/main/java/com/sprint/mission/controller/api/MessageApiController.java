package com.sprint.mission.controller.api;

import com.sprint.mission.dto.message.MessageCreateRequestDto;
import com.sprint.mission.dto.message.MessageResponseDto;
import com.sprint.mission.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.application.message.MessageApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
//메시지 관리
//[X] 메시지를 보낼 수 있다.
//[X] 메시지를 수정할 수 있다.
//[X] 메시지를 삭제할 수 있다.
//[X] 특정 채널의 메시지 목록을 조회할 수 있다.
public class MessageApiController {

    private final MessageApplicationService messageApplicationService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> create(
            @Valid @RequestPart("message")                                  MessageCreateRequestDto request,
            @Valid @RequestPart(value = "attachments", required = false)    List<MultipartFile> attachments
    ) {
        MessageResponseDto createdMessage =
                messageApplicationService.create(
                        request,
                        attachments
                );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMessage);
    }


    @PutMapping
    public ResponseEntity<MessageResponseDto> update(
            @Valid @RequestParam UUID messageId,
            @Valid @RequestBody MessageUpdateRequestDto request
    ) {
        MessageResponseDto updatedMessage = messageApplicationService.update(messageId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMessage);
    }


    @DeleteMapping
    public ResponseEntity<Void> delete(
            @Valid @RequestParam UUID messageId
    ) {
        messageApplicationService.delete(messageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }


    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> retrieveAllFromChannel(
            @Valid @RequestParam UUID channelId
    ) {
        List<MessageResponseDto> channelMessageList = messageApplicationService.findAllByChannelId(channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channelMessageList);
    }
}
