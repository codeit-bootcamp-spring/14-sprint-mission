package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-24T15:39:02+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.18 (Oracle Corporation)"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UserStatusDto toDto(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        Instant lastActiveAt = null;
        UUID id = null;

        userId = userStatusUserId( userStatus );
        lastActiveAt = userStatus.getLastAccessAt();
        id = userStatus.getId();

        UserStatusDto userStatusDto = new UserStatusDto( id, userId, lastActiveAt );

        return userStatusDto;
    }

    private UUID userStatusUserId(UserStatus userStatus) {
        User user = userStatus.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
