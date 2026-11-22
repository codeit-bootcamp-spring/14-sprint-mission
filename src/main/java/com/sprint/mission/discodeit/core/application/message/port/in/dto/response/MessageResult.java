package com.sprint.mission.discodeit.core.application.message.port.in.dto.response;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentResult;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.domain.message.Message;
import com.sprint.mission.discodeit.core.domain.user.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResult(UUID id,
                            Instant createdAt,
                            Instant updatedAt,
                            String content,
                            UUID channelId,
                            UserResult author,
                            List<BinaryContentResult> attachments) {
    public static MessageResult from(Message message, boolean authorOnline) {
        User author = message.getAuthor();
        return new MessageResult(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                author == null ? null : UserResult.from(author, authorOnline),
                message.getAttachments().stream()
                        .map(BinaryContentResult::from)
                        .toList()
        );
    }
}
