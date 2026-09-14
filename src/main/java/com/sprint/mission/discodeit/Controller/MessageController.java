package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.IService.MessageService;
import com.sprint.mission.discodeit.util.BinaryContentMapper;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final BinaryContentMapper binaryContentMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseDto> create(
        @RequestPart("messageCreateRequest") MessageCreateRequestDto request,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<BinaryContentCreateRequestDto> attachmentRequests = attachments == null
            ? List.of() : attachments.stream()
                .map(binaryContentMapper::toBinaryContentCreateRequestDto)
                .toList();
        MessageResponseDto response = messageService.create(request, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @RequestMapping(method = RequestMethod.PATCH, path = "/{messageId}")
    public ResponseEntity<MessageResponseDto> update(
        @PathVariable UUID messageId,
        @RequestBody MessageUpdateRequestDto request
    ) {
        MessageResponseDto response = messageService.update(messageId, request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PageResponse<MessageResponseDto>> findAllByChannelId(
        @RequestParam UUID channelId,
        @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<MessageResponseDto> response = messageService.findAllByChannelId(channelId, pageable);
        return ResponseEntity.ok(response);
    }
}
