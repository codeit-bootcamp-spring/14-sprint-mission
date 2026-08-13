package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadStatusCreateRequestDto {
    UUID userId;
    UUID channelId;

    public static ReadStatusCreateRequestDto from(
            UUID userId,
            UUID channelId
    ) {
        if (Objects.isNull(userId)) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }
        if (Objects.isNull(channelId)) {
            throw new CustomException(ExceptionType.CHANNEL_ID_IS_NULL);
        }

        return new ReadStatusCreateRequestDto(userId, channelId);
    }
}
