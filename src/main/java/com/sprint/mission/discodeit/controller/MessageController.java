package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
            @RequestPart("messageCreateRequest") MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        MessageDto created = messageService.create(request, resolveAttachmentRequests(attachments));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/find")
    public ResponseEntity<MessageDto> find(@RequestParam UUID messageId) {
        return ResponseEntity.ok(messageService.find(messageId));
    }

    @GetMapping("/findAllByChannelId")
    public ResponseEntity<List<MessageDto>> findAllByChannelId(@RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @PatchMapping("/update")
    public ResponseEntity<MessageDto> update(@RequestParam UUID messageId, @RequestBody MessageUpdateRequest request) {
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    private List<BinaryContentCreateRequest> resolveAttachmentRequests(List<MultipartFile> attachments) {
        if (attachments == null) {
            return List.of();
        }
        return attachments.stream()
                .map(file -> {
                    try {
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(), file.getContentType(), file.getBytes());
                    } catch (IOException e) {
                        throw new UncheckedIOException("첨부파일을 읽을 수 없습니다.", e);
                    }
                })
                .toList();
    }
}
