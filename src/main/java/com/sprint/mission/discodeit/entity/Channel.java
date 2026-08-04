package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String channelName;
    private List<UUID> memberIds;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, List<UUID> memberIds) {
        this.channelName = channelName;
        this.memberIds = memberIds;

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
