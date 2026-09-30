package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.user.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageCreateService {
    private final UserService userService;
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;
    private final MessageService messageService;

    public Message create(String content, UUID channelId, UUID userId,
                          List<CreateBinaryContentCommand> createFileCommands) {
        User user = userService.findById(userId);
        Channel channel = channelService.findById(channelId);

        if (channel.isPrivate() && !readStatusService.existsByUserAndChannel(userId, channelId)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }

        List<BinaryContent> createdAttachments = createFilesIfNotNull(createFileCommands);
        return messageService.create(
                Message.of(
                        content,
                        user,
                        channel,
                        createdAttachments
                )
        );
    }

    private @Nullable List<BinaryContent> createFilesIfNotNull(List<CreateBinaryContentCommand> createFileCommands) {
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
}
