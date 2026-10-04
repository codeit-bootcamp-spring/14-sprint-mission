package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.common.multipart.MultiPartFileUtil;
import com.sprint.mission.discodeit.dto.common.PageResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.application.MessageApplication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
public class MessageApiController {
    private final MessageApplication messageApplication;
    private final MultiPartFileUtil multiPartFileUtil;

    // 1. 메세지를 보낼 수 있다.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MessageResponseDto createMessage(@Valid @RequestPart MessageCreateRequest messageCreateRequest,
                                            @RequestPart(required = false) List<MultipartFile> attachments) {
        List<CreateBinaryContentCommand> createFileCommands = createCommandIfNotNull(attachments);
        return messageApplication.createMessage(
                messageCreateRequest.content(),
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                createFileCommands
        );
    }

    private List<CreateBinaryContentCommand> createCommandIfNotNull(List<MultipartFile> files) {
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }
        return files.stream()
                .map(this::createCommandIfNotNull)
                .toList();
    }

    private CreateBinaryContentCommand createCommandIfNotNull(MultipartFile file) {
        if (Objects.isNull(file)) {
            return null;
        }
        return multiPartFileUtil.convert(file);
    }

    // 2. 메세지를 수정할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{messageId}")
    public MessageResponseDto updateMessage(@PathVariable UUID messageId,
                                            @Valid @RequestBody MessageUpdateDto request) {
        return messageApplication.updateMessage(messageId, request.newContent());
    }

    // 3. 메세지를 삭제할 수 있다.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{messageId}")
    public MessageResponseDto deleteMessage(@PathVariable UUID messageId) {
        return messageApplication.deleteMessage(messageId);
    }

    // 4. 특정 채널의 메세지 목록을 조회할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PageResponse<MessageResponseDto, Instant> getChannelMessages(
            @RequestParam UUID channelId,
            @RequestParam(required = false) LocalDateTime cursor,
            @PageableDefault(
                    size = 50,
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Instant instantCursor = Optional.ofNullable(cursor)
                .orElse(LocalDateTime.now(ZoneId.systemDefault()))
                .atZone(ZoneId.systemDefault()).toInstant();

        return messageApplication.getAllByChannelId(
                channelId,
                instantCursor,
                pageable
        );
    }
}
