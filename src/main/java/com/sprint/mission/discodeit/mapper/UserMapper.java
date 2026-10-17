package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = BinaryContentMapper.class
)
public interface UserMapper {
    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "profile", source = "user.profileImage")
    @Mapping(target = "online", expression = "java(user.getUserStatus().isActive())")
    UserResponseDTO toResponse(User user);
}
