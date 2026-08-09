package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType channelType;

    // ChannelCreateDto를 통해 Channel 객체 생성, 따라서 private으로
    private Channel(
            String name,
            String description,
            ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.description = description;
        this.channelType = channelType;
    }

    // Public Channel 객체를 생성하는 정적 팩토리 메소드
    public static Channel createPublic(String name, String description) {
        return new Channel(
                name,
                description,
                ChannelType.PUBLIC
        );
    }

    // Public Channel 객체를 생성하는 정적 팩토리 메소드
    public static Channel createPrivate() {
        return new Channel(
                null,
                null,
                ChannelType.PRIVATE
        );
    }

    public void updateNameAndDescription(Channel channelUpdates) {
        // 뭔가 수정 필요
        boolean isUpdated = false;

        // channel name/ description이 그대로거나 비어있으면 update 안된거임
        if (Objects.nonNull(channelUpdates.getName()) &&
            !Objects.equals(channelUpdates.getName(), name)
        ) {
            isUpdated = true;
            this.name = channelUpdates.getName();
        }

        if (Objects.nonNull(channelUpdates.getDescription()) &&
            !Objects.equals(channelUpdates.getDescription(), description)
        ) {
            isUpdated = true;
            this.description = channelUpdates.getDescription();
        }

        // 수정 됐다고 표시 (updatedAt 시간 변경)
        if (isUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
