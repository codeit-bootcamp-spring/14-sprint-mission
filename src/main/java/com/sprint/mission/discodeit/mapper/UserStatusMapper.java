package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.user.domain.entity.UserStatus;
import com.sprint.mission.discodeit.user.web.dto.res.UserStatusResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {
    @Mapping(target = "userId", source = "user.id")
    UserStatusResponseDTO toResponse(UserStatus userStatus);
}
