package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto send(@RequestBody MessageCreateRequestDto dto) {
        return messageService.createMessage(dto);
    }

    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto sendWithAttachments(@RequestPart("messageCreateRequest") MessageCreateRequestDto dto,
                                                  @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return messageService.createMessage(dto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
    public MessageResponseDto modify(@PathVariable UUID messageId,
                                     @RequestBody MessageUpdateRequestDto dto) {
        return messageService.updateMessage(messageId, dto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<MessageResponseDto> getMessageListByChannelId(@RequestParam UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}
