package com.sprint.mission.discodeit.core.application.message.port.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.message.port.in.dto.response.MessageResult;
import com.sprint.mission.discodeit.core.common.pagination.PageResult;
import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
public interface MessageUseCase {
    MessageResult createMessage(@NotBlank String content, @NotNull UUID channelId, @NotNull UUID userId,
                                @Nullable List<@NotNull CreateBinaryContentCommand> attachments);

    PageResult<MessageResult, Instant> getAllByChannelId(@NotNull UUID channelId, @Nullable Instant cursor,
                                                         @NotNull PageQuery pageQuery);

    MessageResult updateMessage(@NotNull UUID id, @NotBlank String content, @NotNull UUID userId);

    MessageResult deleteMessage(@NotNull UUID id, @NotNull UUID userId);
}
