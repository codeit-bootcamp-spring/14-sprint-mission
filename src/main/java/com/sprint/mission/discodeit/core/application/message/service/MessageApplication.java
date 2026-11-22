package com.sprint.mission.discodeit.core.application.message.service;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.channel.port.out.ChannelPort;
import com.sprint.mission.discodeit.core.application.message.port.in.MessageUseCase;
import com.sprint.mission.discodeit.core.application.message.port.in.dto.response.MessageResult;
import com.sprint.mission.discodeit.core.application.message.port.out.MessagePort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserSessionPort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.message.Message;
import com.sprint.mission.discodeit.core.domain.user.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.*;

@ApplicationService
@RequiredArgsConstructor
public class MessageApplication implements MessageUseCase {
    private final MessagePort messages;
    private final ChannelPort channels;
    private final UserPort users;
    private final UserSessionPort userSessions;
    private final ReadStatusPort readStatuses;
    private final BinaryContentStoragePort binaryContentStoragePort;

    @Override
    @PreAuthorize("principal.userId == #userId")
    @MutationLog
    @Transactional
    public MessageResult createMessage(String content, UUID channelId, UUID userId,
                                       List<CreateBinaryContentCommand> createFileCommands) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);

        boolean participating = readStatuses.existsByUserAndChannel(userId, channelId);
        channel.validateAccessible(participating, userId);

        List<BinaryContent> createdAttachments = createFiles(createFileCommands);
        Message created = messages.save(
                Message.of(
                        content,
                        user,
                        channel,
                        createdAttachments
                )
        );

        saveCreatedAttachments(createdAttachments, createFileCommands);

        return toResult(created);
    }

    private void saveCreatedAttachments(List<BinaryContent> createdAttachments, List<CreateBinaryContentCommand> createFileCommands) {
        if (Objects.nonNull(createFileCommands) && !createFileCommands.isEmpty()) {
            for (int i = 0; i < createFileCommands.size(); i++) {
                UUID id = createdAttachments.get(i).getId();
                byte[] content = createFileCommands.get(i).content();
                binaryContentStoragePort.put(id, content);
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

    @Override
    @Transactional
    public Slice<MessageResult> getAllByChannelId(
            UUID channelId,
            Instant cursor,
            Pageable pageable
    ) {
        channels.get(channelId);
        return messages.findAllByChannelId(
                        channelId,
                        Optional.ofNullable(cursor).orElse(Instant.now(Clock.systemDefaultZone())),
                        pageable
                )
                .map(this::toResult);
    }

    @Override
    @PreAuthorize("principal.userId == #userId")
    @MutationLog
    @Transactional
    public MessageResult updateMessage(UUID id, String content, UUID userId) {
        Message updating = messages.get(id);
        validateAuthor(updating, userId);
        return toResult(updating.updateContent(content));
    }

    @Override
    @PreAuthorize("principal.userId == #userId")
    @MutationLog
    @Transactional
    public MessageResult deleteMessage(UUID id, UUID userId) {
        Message deleting = messages.get(id);
        validateAuthor(deleting, userId);
        messages.delete(deleting);
        return toResult(deleting);
    }

    private void validateAuthor(Message message, UUID userId) {
        if (!message.isAuthorBy(userId)) {
            throw new AccessDeniedException("메시지 작성자만 수정하거나 삭제할 수 있습니다.");
        }
    }

    private MessageResult toResult(Message message) {
        User author = message.getAuthor();
        boolean isOnline = Objects.nonNull(author)
                && userSessions.isOnline(author.getId());
        return MessageResult.from(message, isOnline);
    }
}
