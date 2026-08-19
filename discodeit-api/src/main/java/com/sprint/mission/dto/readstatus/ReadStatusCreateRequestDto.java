package com.sprint.mission.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadStatusCreateRequestDto {

    @NotBlank
    UUID userId;

    @NotBlank
    UUID channelId;

}
