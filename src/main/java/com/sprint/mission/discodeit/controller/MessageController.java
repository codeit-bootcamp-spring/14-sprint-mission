package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
        @RequestPart("messageCreateRequest") @Valid MessageCreateRequest request,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("create 요청. channelId:{}", request.channelId());
        List<BinaryContentCreateRequest> attachmentRequests = (attachments == null)
            ? List.of()
            : attachments.stream().map(this::toBinaryContentCreateRequest).toList();
        MessageDto created = messageService.create(request, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> update(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequest request) {
        log.info("update 요청. messageId:{}", messageId);
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        log.info("delete 요청. messageId:{}", messageId);
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
        @RequestParam UUID channelId,
        @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {
        log.info("findAllByChannelId 요청. channelId:{}, pageable:{}", channelId, pageable);
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId, pageable));
    }

    private BinaryContentCreateRequest toBinaryContentCreateRequest(MultipartFile file) {
        try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                "첨부파일을 읽을 수 없습니다: " + file.getOriginalFilename());
        }
    }
}
