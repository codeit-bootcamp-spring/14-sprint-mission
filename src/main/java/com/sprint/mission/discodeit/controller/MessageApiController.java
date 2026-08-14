package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageAndAttachmentsCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.application.message.MessageApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public MessageResponseDto create(

            @Valid @RequestPart("message")
            MessageCreateRequestDto request,

            @Valid @RequestPart(
                    value = "attachments",
                    required = false
            )
            List<MultipartFile> attachments
    ) {
        return messageApplicationService.create(
                request,
                attachments
        );
    }


    @PutMapping("/api/messages/{id}")
    public MessageResponseDto update(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody MessageUpdateRequestDto request
    ) {
        return messageApplicationService.update(id, request);
    }


    @DeleteMapping("/api/messages/{id}")
    public void delete(@PathVariable UUID id) {
        messageApplicationService.delete(id);
    }


    @GetMapping("/api/channels/{channelId}/messages")
    public List<MessageResponseDto> retrieveAllFromChannel(
            @PathVariable UUID channelId
    ) {
        return messageApplicationService.findAllByChannelId(channelId);
    }
}
