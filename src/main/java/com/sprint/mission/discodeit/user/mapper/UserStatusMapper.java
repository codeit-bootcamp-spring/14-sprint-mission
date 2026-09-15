package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusMapper {

    public UserStatusDto toDto(UserStatus userStatus){
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastAccessAt()
        );
    }

}
