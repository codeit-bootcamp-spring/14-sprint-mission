package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.GET, value = "api/messages")
    public ResponseEntity<List<MessageResponseDto>> findAll(
        @RequestParam UUID channelId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.findAllByChannelId(channelId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/messages")
    public ResponseEntity<MessageResponseDto> create(
        @Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.messageCreate(messageCreateRequestDto));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/messages/{messageId}")
    public void update(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequestDto messageUpdateRequestDto) {
        messageService.messageUpdate(messageId, messageUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/messages/{messageId}")
    public void delete(
        @PathVariable UUID messageId) {
        messageService.messageDelete(messageId);
    }
}
