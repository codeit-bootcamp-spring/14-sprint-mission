package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lastActiveAt", source = "lastAccessAt")
    UserStatusDto toDto(UserStatus userStatus);

}
