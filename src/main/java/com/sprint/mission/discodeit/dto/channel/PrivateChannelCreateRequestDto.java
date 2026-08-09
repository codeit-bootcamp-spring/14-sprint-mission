package com.sprint.mission.discodeit.dto.channel;


import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrivateChannelCreateRequestDto {
    List<UUID> participantUserIds;

    public static PrivateChannelCreateRequestDto of(List<UUID> participantUserIds) {
        if (Objects.isNull(participantUserIds) || participantUserIds.isEmpty()) {
            throw new CustomException(ExceptionType.PRIVATE_CHANNEL_PARTICIPANTS_IS_EMPTY);
        }
        if (participantUserIds.stream().anyMatch(Objects::isNull)) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }
        return new PrivateChannelCreateRequestDto(
                List.copyOf(participantUserIds)
        );
    }
}
