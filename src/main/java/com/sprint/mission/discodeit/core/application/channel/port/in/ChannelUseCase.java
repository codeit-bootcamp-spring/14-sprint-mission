package com.sprint.mission.discodeit.core.application.channel.port.in;

import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelResult;
import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelUpsertResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface ChannelUseCase {
    ChannelUpsertResult createPublicChannel(@NotBlank String name, @NotBlank String description);

    ChannelUpsertResult createPrivateChannel(@NotEmpty List<@NotNull UUID> userIds);

    List<ChannelResult> getAllChannelsByUserId(@NotNull UUID userId);

    ChannelUpsertResult updateChannelName(@NotNull UUID id, @NotBlank String name,
                                          @NotNull String description);

    void deleteChannel(@NotNull UUID id);
}
