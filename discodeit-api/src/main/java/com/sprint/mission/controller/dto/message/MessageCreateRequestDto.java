package com.sprint.mission.controller.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {

    @NotNull
    String content;

    @NotNull
    UUID senderId;

    @NotNull
    UUID channelId;

}
