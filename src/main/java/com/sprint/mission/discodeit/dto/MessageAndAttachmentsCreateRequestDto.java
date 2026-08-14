package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageAndAttachmentsCreateRequestDto {

    @NotNull
    private MessageCreateRequestDto message;

    private List<BinaryContentCreateRequestDto> attachments;
}
