package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access =AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChannelResponseDto {
    UUID id;
    String name;
    ChannelType channelType;
    Long updatedAt;

    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getChannelType(),
                channel.getUpdatedAt()
        );
    }
}
