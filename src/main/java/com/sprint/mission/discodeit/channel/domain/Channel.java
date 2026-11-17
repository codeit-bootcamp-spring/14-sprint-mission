package com.sprint.mission.discodeit.channel.domain;

import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.Instant;

@Getter
@Entity
@Table(name = "channels")
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Channel(ChannelType channelType, String name, String description) {
        super();
        this.channelType = channelType;
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        this.updateUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
