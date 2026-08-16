package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public MessageResponseDto send(@RequestBody MessageCreateRequestDto dto) {
        return messageService.createMessage(dto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public MessageResponseDto modify(@PathVariable UUID id,
                                     @RequestBody MessageUpdateRequestDto dto) {
        return messageService.updateMessage(id, dto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable UUID id) {
        messageService.deleteMessage(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<MessageResponseDto> getMessageListByChannelId(@RequestParam UUID id) {
        return messageService.findAllByChannelId(id);
    }
}
