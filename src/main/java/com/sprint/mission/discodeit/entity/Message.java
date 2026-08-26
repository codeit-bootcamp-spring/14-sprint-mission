package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message extends UpdatableEntity {
    private static final long serialVersionUID = 1L;

    String contents;
    final UUID authorId;
    final UUID channelId;
    List<UUID> attachmentIds;

    private Message(String contents, UUID authorId, UUID channelId,List<UUID> attachmentIds) {
        super();
        this.contents = contents;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public static Builder builder() {
        return new Builder();
    }


    public String update(String contents) {
        if(contents == null || contents.isBlank()) {
            throw new DiscodeitException(ErrorCode.INVALID_MESSAGE_CONTENT);
        }
        this.contents = contents;
        updateTimeStamp();
        return this.contents;
    }

    public void updateAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds;
        updateTimeStamp();
    }


    @Override
    public String toString() {
        return this.contents;
    }

    public static class Builder {
        private String contents;
        private UUID authorId;
        private UUID channelId;
        private List<UUID> attachmentIds;

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder authorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder channelId(UUID channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder attachmentIds(List<UUID> attachmentIds) {
            this.attachmentIds = attachmentIds;
            return this;
        }

        public Message build() {
            return new Message(this.contents, this.authorId, this.channelId, this.attachmentIds);
        }


    }

}
