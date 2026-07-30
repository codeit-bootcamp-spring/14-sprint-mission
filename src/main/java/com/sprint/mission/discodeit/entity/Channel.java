package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Channel extends BaseEntity{

    final UUID creatorId;
    String name;

    private Channel(UUID creatorId, String name) {
        super();
        this.creatorId = creatorId;
        this.name = name;
    }

    public static Channel create(UUID creatorId, String name){
        return new Channel(creatorId, name);
    }

    public void changeName(String name){
        this.name = name;
        newUpdatedAt();
    }
}
