package com.sprint.mission.discodeit.entity;


import java.time.Instant;
import lombok.Getter;

@Getter
public class Channel extends BasicEntity {

    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }
    public Channel(ChannelType type) {
        super();
        this.type = type;
    }


    public void update(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }


    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Channel{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", name='" + name + '\'' +
            '}';
    }


}
