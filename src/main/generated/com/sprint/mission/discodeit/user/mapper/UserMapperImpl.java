package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.UserDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-24T15:39:02+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.18 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private BinaryContentMapper binaryContentMapper;

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        Boolean online = null;
        UUID id = null;
        String email = null;
        BinaryContentDto profile = null;

        username = user.getUserName();
        online = userUserStatusOnline( user );
        id = user.getId();
        email = user.getEmail();
        profile = binaryContentMapper.toDto( user.getProfile() );

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }

    private Boolean userUserStatusOnline(User user) {
        UserStatus userStatus = user.getUserStatus();
        if ( userStatus == null ) {
            return null;
        }
        return userStatus.isOnline();
    }
}
