package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ToString
@Getter
public final class Message extends ModifiableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String content;
    @ToString.Include
    private final UUID userId;
    @ToString.Include
    private final UUID channelId;

    private final List<UUID> attachmentIds = new ArrayList<>();

    public Message(String content, UUID userId, UUID channelId, @Nullable List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;

        if (Objects.nonNull(attachmentIds)) {
            this.attachmentIds.addAll(attachmentIds);
        }
    }

    public Message(String content, UUID userId, UUID channelId) {
        this(content, userId, channelId, null);
    }

    public void updateContent(String content) {
        this.content = content;

        super.markedAsUpdate();
    }
}
