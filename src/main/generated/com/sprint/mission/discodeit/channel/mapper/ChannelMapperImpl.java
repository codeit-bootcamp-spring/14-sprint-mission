package com.sprint.mission.discodeit.channel.mapper;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
public class ChannelMapperImpl implements ChannelMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt) {
        if ( channel == null && participants == null && lastMessageAt == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        if ( channel != null ) {
            id = channel.getId();
            type = channel.getChannelType();
            name = channel.getName();
            description = channel.getDescription();
        }
        List<UserDto> participants1 = null;
        participants1 = userListToUserDtoList( participants );
        Instant lastMessageAt1 = null;
        lastMessageAt1 = lastMessageAt;

        ChannelDto channelDto = new ChannelDto( id, type, name, description, participants1, lastMessageAt1 );

        return channelDto;
    }

    protected List<UserDto> userListToUserDtoList(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDto> list1 = new ArrayList<UserDto>( list.size() );
        for ( User user : list ) {
            list1.add( userMapper.toDto( user ) );
        }

        return list1;
    }
}
