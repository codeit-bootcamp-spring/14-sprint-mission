package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import java.util.List;

public record PageResponseMessageDto(
    List<MessageDto> content,
    int number,
    int size,
    Long totalElements
) {
}
