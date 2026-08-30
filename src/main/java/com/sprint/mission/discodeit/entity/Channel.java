package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt;
    private ChannelType type;
    private String channelName;
    private String description;


    public static Channel createPublic(PublicChannelCreateRequest request) {
        Channel channel = new Channel();
        channel.description = request.description();
        channel.channelName = request.name();
        channel.type = ChannelType.PUBLIC;
        return channel;
    }

    public static Channel createPrivate() {
        Channel channel = new Channel();
        channel.type = ChannelType.PRIVATE;
        return channel;
    }


    public Channel update(ChannelRequest request) {
        if (this.getType() == ChannelType.PRIVATE) {
            throw new DiscodeitRuntimeException(ExceptionType.INVALID_INFO);
        }
        this.channelName = request.newName();
        this.description = request.newDescription();
        this.updatedAt = Instant.now();
        return this;
    }

    private ChannelDto toDto(Channel channel, Instant lastMessageAt, List<UUID> participantIds) {
        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getChannelName(),
            channel.getDescription(),
            participantIds,
            lastMessageAt
        );
    }

    @Override
    public String toString() {
        return "채널 이름 : " + this.channelName + " 채널 유형 : " + this.type;
    }

}
