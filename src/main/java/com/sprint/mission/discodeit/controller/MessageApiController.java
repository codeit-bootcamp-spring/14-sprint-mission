package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.common.multipart.MultiPartFileUtil;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.application.MessageApplication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
public class MessageApiController {
    private final MessageApplication messageApplication;
    private final MultiPartFileUtil multiPartFileUtil;

    // 1. 메세지를 보낼 수 있다.
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public MessageResponseDto createMessage(@Valid @RequestPart MessageCreateRequest messageCreateRequest,
                                            @RequestPart(required = false) List<MultipartFile> attachments) {
        List<CreateBinaryContentCommand> createFileCommands = createCommandIfNotNullOrElseGetNull(attachments);
        return messageApplication.createMessage(
                messageCreateRequest.content(),
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                createFileCommands
        );
    }

    private List<CreateBinaryContentCommand> createCommandIfNotNullOrElseGetNull(List<MultipartFile> files) {
        return Objects.nonNull(files) ?
                files.stream()
                        .map( multiPartFileUtil::convert)
                        .toList() : null;
    }

    // 2. 메세지를 수정할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
    public MessageResponseDto updateMessage(@PathVariable UUID messageId,
                                            @Valid @RequestBody MessageUpdateDto request) {
        return messageApplication.updateMessage(messageId, request.newContent());
    }

    // 3. 메세지를 삭제할 수 있다.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
    public MessageResponseDto deleteMessage(@PathVariable UUID messageId) {
        return messageApplication.deleteMessage(messageId);
    }

    // 4. 특정 채널의 메세지 목록을 조회할 수 있다.
    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResponseDto> getChannelMessages(@RequestParam UUID channelId) {
        return messageApplication.getAllByChannelId(channelId);
    }
}
