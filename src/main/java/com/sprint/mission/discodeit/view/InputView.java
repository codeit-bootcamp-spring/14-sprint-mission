package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;

import java.util.UUID;

public abstract class InputView {

    public final UserCreateRequestDto createUser() {
        String username = this.readString();
        String email = this.readString();
        String password = this.readString();

        return UserCreateRequestDto.of(username, email, password);
    }

    public final UserUpdateRequestDto updateUser() {
        UUID id = this.readUUID();
        String username = this.readString();
        String email = this.readString();
        String password = this.readString();

        return UserUpdateRequestDto.of(id, username, email, password);
    }

    public final ChannelCreateRequestDto createChannel() {
        String name = this.readString();
        ChannelType channelType = this.readChannelType();

        return ChannelCreateRequestDto.of(name, channelType);
    }

    public final ChannelUpdateRequestDto updateChannel() {
        UUID id = this.readUUID();
        String name = this.readString();
        ChannelType channelType = this.readChannelType();

        return ChannelUpdateRequestDto.of(id, name, channelType);
    }

    public final MessageCreateRequestDto createMessage() {
        String content = this.readString();
        UUID senderId = this.readUUID();
        UUID channelId = this.readUUID();

        return MessageCreateRequestDto.of(content, senderId, channelId);
    }

    public final MessageUpdateRequestDto updateMessage() {
        UUID senderId = this.readUUID();
        String content = this.readString();

        return MessageUpdateRequestDto.of(senderId, content);
    }


    protected abstract String readOperation();

    protected abstract UUID readUUID();
    protected abstract String readString();
    protected abstract ChannelType readChannelType();

}
