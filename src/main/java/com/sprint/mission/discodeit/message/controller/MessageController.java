package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.application.MessageService;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto create(@Valid @RequestPart(value = "messageCreateRequest") MessageCreateRequestDto request,
                             @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments){
        log.debug("메시지 생성 요청 - channelId : {}, authorId : {}, attachments : {}",
                request.channelId(), request.authorId(), attachments == null ? 0 : attachments.size());
        return messageService.create(request, attachments);
    }

    @PatchMapping(value = "/{messageId}")
    public MessageDto update(@PathVariable UUID messageId,
                                     @Valid @RequestBody MessageUpdateRequestDto request){
        log.debug("메시지 수정 요청 - messageId : {}", messageId);
        return messageService.update(messageId, request);
    }

    @DeleteMapping(value = "/{messageId}")
    public void delete(@PathVariable UUID messageId){
        log.debug("메시지 삭제 요청 - messageId : {}", messageId);
        messageService.delete(messageId);
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
            @RequestParam UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        log.debug("채널별 메시지 목록 조회 요청 - channelId : {}, cursor : {}", channelId, cursor);
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId, cursor, pageable));
    }


}
