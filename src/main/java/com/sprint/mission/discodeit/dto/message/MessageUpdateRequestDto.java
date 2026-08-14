package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageUpdateRequestDto {

    @NotBlank
    String content;

}
