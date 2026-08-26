package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private BinaryContentCreateRequest toBinaryContentCreateRequest(
        MultipartFile file) {
        try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("첨부파일 변환 중 오류가 발생했습니다.", e);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
        @RequestPart("messageCreateRequest") @Valid MessageCreateRequest request,
        @RequestPart(value = "attachments", required = false)
        List<MultipartFile> attachments
    ) {
        log.info("Message create 정상 작동");
        List<BinaryContentCreateRequest> attachmentRequests = (attachments == null)
            ? List.of() : attachments.stream().map(this::toBinaryContentCreateRequest)
            .toList();
        MessageDto created = messageService.create(request, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(created);
    }


    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> update(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequest request) {

        log.info("update 정상 작동. 수정할 메세지id:{}", messageId);
        MessageDto updated = messageService.update(messageId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{messageId}")
    public void delete(@PathVariable UUID messageId) {
        log.info("delete 정상 작동. 삭제할 messageId:{}", messageId);
        messageService.delete(messageId);
    }

    @GetMapping
    public ResponseEntity<List<MessageDto>> findAllByChannelId(
        @RequestParam UUID channelId) {
        log.info("findAllByChannelId 정상 작동. 조회할 channelId:{}", channelId);
        List<MessageDto> messages
            = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }

}
