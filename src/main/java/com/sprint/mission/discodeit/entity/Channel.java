package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class Channel extends Entity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;
    @ToString.Include
    private final List<UUID> usersId;
    @ToString.Include
    private final List<UUID> messagesId;

    public Channel(String name, List<UUID> usersId) {
        super();
        this.name = name;
        this.usersId = new ArrayList<>(usersId);
        messagesId = new ArrayList<>();
    }

    public void updateName(String name) {
        super.updatedAt = now();
        this.name = name;
    }

    public void addMessage(UUID userId ,UUID messageId) {
        if (!usersId.contains(userId)) {
            throw new IllegalArgumentException("채널에 없는 유저는 메시지 보낼 수 없음");
        }

        messagesId.add(messageId);
    }
}
