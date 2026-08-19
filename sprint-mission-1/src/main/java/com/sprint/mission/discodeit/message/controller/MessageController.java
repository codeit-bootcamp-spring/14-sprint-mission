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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/messages")
    public ResponseEntity<List<MessageResponseDto>> findAll(
        @RequestParam UUID channelId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.findAllByChannelId(channelId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST,
        value = "/api/messages",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseDto> create(
        @Valid @RequestPart(value = "messageCreateRequest") MessageCreateRequestDto messageCreateRequestDto,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.messageCreate(messageCreateRequestDto, attachments));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/messages/{messageId}")
    public ResponseEntity<MessageResponseDto> update(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequestDto messageUpdateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.messageUpdate(messageId, messageUpdateRequestDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/messages/{messageId}")
    public ResponseEntity<Void> delete(
        @PathVariable UUID messageId) {
        messageService.messageDelete(messageId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
