package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.application.message.MessageApplicationService;
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
//메시지 관리
//[X] 메시지를 보낼 수 있다.
//[X] 메시지를 수정할 수 있다.
//[X] 메시지를 삭제할 수 있다.
//[X] 특정 채널의 메시지 목록을 조회할 수 있다.
public class MessageApiController {

    private final MessageApplicationService messageApplicationService;

    @PostMapping("/api/messages")
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


    @PutMapping("/api/messages")
    public ResponseEntity<MessageResponseDto> update(
            @Valid @RequestParam UUID messageId,
            @Valid @RequestBody MessageUpdateRequestDto request
    ) {
        MessageResponseDto updatedMessage = messageApplicationService.update(messageId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMessage);
    }


    @DeleteMapping("/api/messages")
    public ResponseEntity<Void> delete(
            @Valid @RequestParam UUID messageId
    ) {
        messageApplicationService.delete(messageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }


    @GetMapping("/api/messages")
    public ResponseEntity<List<MessageResponseDto>> retrieveAllFromChannel(
            @Valid @RequestParam UUID channelId
    ) {
        List<MessageResponseDto> channelMessageList = messageApplicationService.findAllByChannelId(channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channelMessageList);
    }
}
