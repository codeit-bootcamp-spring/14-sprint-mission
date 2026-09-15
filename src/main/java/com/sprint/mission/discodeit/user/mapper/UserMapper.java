package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                binaryContentMapper.toDto(user.getProfile()),
                user.getUserStatus().isOnline()
        );
    }
}
