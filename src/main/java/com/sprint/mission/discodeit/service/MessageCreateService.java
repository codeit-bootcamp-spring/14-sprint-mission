package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
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
    private final BinaryContentService binaryContentService;

    public Message create(String content, UUID channelId, UUID userId,
                          List<CreateBinaryContentCommand> createFileCommands) {
        userService.validateExistsById(userId);
        Channel channel = channelService.findById(channelId);

        if (channel.isPrivate() && !readStatusService.existsByUserAndChannel(userId, channelId)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }

        List<UUID> createdAttachmentIds = createFilesAndThenGetIdIfNotNullOrElseGetNull(createFileCommands);

        Message message = new Message(content, userId, channelId, createdAttachmentIds);
        Message created = messageService.create(message);
        channelService.update(channel.getId(), created.getCreatedAt());

        return created;
    }

    private @Nullable List<UUID> createFilesAndThenGetIdIfNotNullOrElseGetNull(List<CreateBinaryContentCommand> createFileCommands) {
        return Objects.nonNull(createFileCommands) ?
                createFileCommands.stream()
                        .map(command -> {
                            return binaryContentService.create(BinaryContent.of(
                                    command.fileName(),
                                    command.contentType(),
                                    command.content()
                            )).getId();
                        }).toList()
                : null;
    }

}
