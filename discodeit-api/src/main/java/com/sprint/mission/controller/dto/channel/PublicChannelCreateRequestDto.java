package com.sprint.mission.controller.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicChannelCreateRequestDto {

    @NotBlank
    String name;

    @NotBlank
    String description;

}
