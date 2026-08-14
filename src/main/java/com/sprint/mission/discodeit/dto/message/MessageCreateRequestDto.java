package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {

    @NotBlank
    String content;

    @NotBlank
    UUID senderId;

    @NotBlank
    UUID channelId;

}
