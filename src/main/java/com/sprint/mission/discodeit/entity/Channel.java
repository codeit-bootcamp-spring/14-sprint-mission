package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;


@Getter
public class Channel extends UpdatableEntity {

    private static final long serialVersionUID = 1L;

    ChannelType type;
    private String channelName;
    private String description;

    private Channel(ChannelType type, String channelName, String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }



    public static Builder builder() {
        return new Builder();
    }

    public String update(String channelName, String description) {
        if (this.type == ChannelType.PRIVATE) {
            throw new DiscodeitException(ErrorCode.PRIVATE_CHANNEL_UPDATE);
        }
        if (channelName == null || channelName.isBlank()) {
            throw new DiscodeitException(ErrorCode.INVALID_CHANNEL_NAME);
        }
        this.channelName = channelName;
        this.description = description;
        updateTimeStamp();
        return this.channelName;
    }

    @Override
    public String toString() {
        return this.channelName;
    }

    public static class Builder {
        private ChannelType type;
        private String channelName;
        private String description;

        public Builder type(ChannelType type) {
            this.type = type;
            return this;
        }
        public Builder channelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Channel build() {
            return new Channel(this.type, this.channelName, this.description);
        }


    }
}
