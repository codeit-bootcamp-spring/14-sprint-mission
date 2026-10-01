package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "channels")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    ChannelType type;

    @Column(length = 100)
    String name;

    @Column(length = 500)
    String description;

    public enum ChannelType {
        PUBLIC, PRIVATE
    }

    private Channel(ChannelType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public static Channel create(ChannelType type, String name, String description) {
        return new Channel(type, name, description);
    }

    public void update(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }

    public void updateType(ChannelType type) {
        if (type != null) {
            this.type = type;
        }
    }
}
