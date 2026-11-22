package com.sprint.mission.discodeit.core.application.message.port.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.message.port.in.dto.response.MessageResult;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
public interface MessageUseCase {
    MessageResult createMessage(@NotBlank String content, @NotNull UUID channelId, @NotNull UUID userId,
                                @Nullable List<@NotNull CreateBinaryContentCommand> attachments);

    Slice<MessageResult> getAllByChannelId(@NotNull UUID channelId, @Nullable Instant cursor,
                                           @NotNull Pageable pageable);

    MessageResult updateMessage(@NotNull UUID id, @NotBlank String content, @NotNull UUID userId);

    MessageResult deleteMessage(@NotNull UUID id, @NotNull UUID userId);
}
