package com.sprint.mission.discodeit.message.web;

import com.sprint.mission.discodeit.global.pagination.PageResponse;
import com.sprint.mission.discodeit.message.application.MessageApplicationService;
import com.sprint.mission.discodeit.message.web.dto.req.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.message.web.dto.req.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.message.web.dto.res.MessageResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageApplicationService messageApplicationService;

    @GetMapping
    public ResponseEntity<PageResponse<MessageResponseDTO>> findAllMessageByChannelId(
        @RequestParam UUID channelId,
        Pageable pageable
    ){
        PageResponse<MessageResponseDTO> response = messageApplicationService
            .findAllMessageByChannelId(channelId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> inputMessage(
        @Valid @RequestPart(value = "messageCreateRequest") MessageCreateRequestDTO request,
        @RequestPart(required = false) List<MultipartFile> attachments
    ){
        MessageResponseDTO response = messageApplicationService.createMessage(request, attachments);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageApplicationService.deleteMessage(messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> updateMessageContent(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequestDTO request
    ){
        MessageResponseDTO response =  messageApplicationService.updateMessageContent(messageId, request.newContent());

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
