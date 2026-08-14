package com.sprint.mission.discodeit.dto.channel;


import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrivateChannelCreateRequestDto {

    @NotEmpty
    List<@NotNull UUID> participantUserIds;

}
