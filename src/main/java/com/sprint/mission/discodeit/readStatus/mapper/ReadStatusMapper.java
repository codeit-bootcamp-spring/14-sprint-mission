package com.sprint.mission.discodeit.readStatus.mapper;

import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadStatusMapper {

    public ReadStatusDto toDto(ReadStatus readStatus){
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadTime()
        );
    }
}
