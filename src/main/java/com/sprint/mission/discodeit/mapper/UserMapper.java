package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }

        boolean isOnline = false;
        if (entity.getStatus() != null && entity.getStatus().getLastActiveAt() != null) {
            isOnline = entity.getStatus().getLastActiveAt()
                .isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
        }

        return new UserDto(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            binaryContentMapper.toDto(entity.getProfile()),
            isOnline
        );
    }
}
