package com.sprint.mission.discodeit.application.message.in;

import com.sprint.mission.discodeit.application.binarycontent.out.BinaryContentStorage;
import com.sprint.mission.discodeit.adapter.in.controller.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.application.channel.ChannelService;
import com.sprint.mission.discodeit.application.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.application.user.UserService;
import com.sprint.mission.discodeit.application.user.AuthService;
import com.sprint.mission.discodeit.common.log.MutationLog;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.application.message.MessageService;
import com.sprint.mission.discodeit.adapter.in.controller.common.PageResponse;
import com.sprint.mission.discodeit.adapter.in.controller.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.domain.user.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageApplication {
    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;
    private final AuthService authService;
    private final ReadStatusService readStatusService;
    private final BinaryContentStorage binaryContentStorage;

    @PreAuthorize("principal.userId == #userId")
    @MutationLog
    @Transactional
    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            List<CreateBinaryContentCommand> createFileCommands) {
        User user = userService.findById(userId);
        Channel channel = channelService.findById(channelId);

        boolean participating = readStatusService.existsByUserAndChannel(userId, channelId);
        channel.validateAccessible(participating, userId);

        List<BinaryContent> createdAttachments = createFiles(createFileCommands);
        Message created = messageService.create(
                Message.of(
                        content,
                        user,
                        channel,
                        createdAttachments
                )
        );

        saveCreatedAttachments(createdAttachments, createFileCommands);

        return toResponse(created);
    }

    private void saveCreatedAttachments(List<BinaryContent> createdAttachments, List<CreateBinaryContentCommand> createFileCommands) {
        if (Objects.nonNull(createFileCommands) && !createFileCommands.isEmpty()) {
            for (int i = 0; i < createFileCommands.size(); i++) {
                UUID id = createdAttachments.get(i).getId();
                byte[] content = createFileCommands.get(i).content();
                binaryContentStorage.put(id, content);
            }
        }
    }

    private @Nullable List<BinaryContent> createFiles(List<CreateBinaryContentCommand> createFileCommands) {
        if (Objects.isNull(createFileCommands) || createFileCommands.isEmpty()) {
            return null;
        }

        return createFileCommands.stream()
                .map(command ->
                        BinaryContent.of(
                                command.fileName(),
                                command.contentType(),
                                command.content()
                        )
                )
                .toList();
    }

    @Transactional
    public PageResponse<MessageResponseDto, Instant> getAllByChannelId(
            UUID channelId,
            Instant cursor,
            Pageable pageable
    ) {
        channelService.validateExists(channelId);
        Slice<MessageResponseDto> retrieved = messageService.findAllByChannelId(
                        channelId,
                        Optional.ofNullable(cursor).orElse(Instant.now(Clock.systemDefaultZone())),
                        pageable
                )
                .map(this::toResponse);

        return PageResponse.fromSlice(
                retrieved,
                MessageResponseDto::createdAt
        );
    }

    @PreAuthorize("@messageService.isAuthor(#id, principal.userId)")
    @MutationLog
    @Transactional
    public MessageResponseDto updateMessage(UUID id, String content) {
        Message updated = messageService.updateContent(id, content);
        return toResponse(updated);
    }

    @PreAuthorize("@messageService.isAuthor(#id, principal.userId)")
    @MutationLog
    @Transactional
    public MessageResponseDto deleteMessage(UUID id) {
        Message deleted = messageService.deleteById(id);
        return toResponse(deleted);
    }

    private MessageResponseDto toResponse(Message message) {
        return MessageResponseDto.of(message, authService.isOnline(message.getAuthor()));
    }
}
