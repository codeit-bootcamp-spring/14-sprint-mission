package com.sprint.mission.discodeit.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public record MessageCreateRequestDto(
    @NotNull
    UUID userId,
    @NotNull
    UUID channelId,
    @NotBlank
    String message,
    List<MultipartFile> attachments) {

}
