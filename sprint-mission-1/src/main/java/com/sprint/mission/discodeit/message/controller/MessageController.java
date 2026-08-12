package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.service.MessageService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/message")
    public MessageResponseDto create(
        @Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
        return messageService.messageCreate(messageCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/message/{id}")
    public void update(
        @PathVariable UUID id,
        @Valid @RequestBody MessageUpdateRequestDto messageUpdateRequestDto) {
        messageService.messageUpdate(id, messageUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/message/{id}/delete")
    public void delete(
        @PathVariable UUID id) {
        messageService.messageDelete(id);
    }
}
