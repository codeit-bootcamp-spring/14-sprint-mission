package com.sprint.mission.discodeit.readStatus.mapper;

import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "channelId", source = "channel.id")
    @Mapping(target = "lastReadAt", source = "lastReadTime")
    ReadStatusDto toDto(ReadStatus readStatus);
}
