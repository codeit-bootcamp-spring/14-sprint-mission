package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.util.Instant;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final Long createdAt = Instant.now();
    private Long updatedAt;
    private ChannelType type;
    private String channelName;
    private String description;



    public static Channel createPublic(PublicChannelCreate request) {
        Channel channel = new Channel();
        channel.description = request.description();
        channel.channelName = request.channelName();
        channel.type = ChannelType.PUBLIC;
        return channel;
    }
    public static Channel createPrivate() {
        Channel channel = new Channel();
        channel.type = ChannelType.PRIVATE;
        return channel;
    }


    public Channel update(ChannelRequest request) {
        if (request.type() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정 불가합니다.");
        }
        this.channelName = request.channelName();
        this.description = request.description();
        this.updatedAt = request.updateAt();
        return this;
    }

    @Override
    public String toString() {
        return "채널 이름 : " + this.channelName + " 채널 유형 : " + this.type;
    }

}
