package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/message")
public class MessageApiController {
    private final BasicMessageService messageService;

    // 1. 메세지를 보낼 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public MessageResponseDto createMessage(@Valid @RequestBody MessageCreationDto request) {
        return messageService.createMessage(
                request.getContent(),
                request.getUserId(),
                request.getChannelId(),
                request.getAttachmentIds()
        );
    }

    // 2. 메세지를 수정할 수 있다.
    @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
    public MessageResponseDto updateMessage(@PathVariable UUID messageId,
                                            @Valid @RequestBody MessageUpdateDto request) {
        return messageService.updateMessage(messageId, request.getContent());
    }

    // 3. 메세지를 삭제할 수 있다.
    @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
    public MessageResponseDto deleteMessage(@PathVariable UUID messageId) {
        return messageService.deleteMessage(messageId);
    }

    // 4. 특정 채널의 메세지 목록을 조회할 수 있다.
    @RequestMapping(method = RequestMethod.GET, value = "/{channelId}")
    public List<MessageResponseDto> getChannelMessages(@PathVariable UUID channelId) {
        return messageService.getAllByChannelId(channelId);
    }
}
