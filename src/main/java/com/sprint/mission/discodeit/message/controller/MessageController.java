package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.application.MessageService;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto create(@RequestPart(value = "messageCreateRequest") MessageCreateRequestDto request,
                             @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments){

        return messageService.create(request, attachments);
    }

    @PatchMapping(value = "/{messageId}")
    public MessageDto update(@PathVariable UUID messageId,
                                     @RequestBody MessageUpdateRequestDto request){

        return messageService.update(messageId, request);
    }

    @DeleteMapping(value = "/{messageId}")
    public void delete(@PathVariable UUID messageId){
        messageService.delete(messageId);
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
            @RequestParam UUID channelId,
            @PageableDefault(size = 50, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable){

        return ResponseEntity.ok(messageService.findAllByChannelId(channelId, pageable));
    }


}
