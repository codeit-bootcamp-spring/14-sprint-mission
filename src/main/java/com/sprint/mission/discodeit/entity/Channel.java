package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.ChannelDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@ToString
@Getter
public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String title;
    private List<User> users;
    private List<Message> messages;

    public Channel(String title, List<User> users) {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = createdAt;

        this.title = title;
        this.users = users;
    }

    public void update(ChannelDto dto) {
        this.updatedAt = now();

        this.title = dto.getTitle();
    }

    private Long now() {
        return System.currentTimeMillis();
    }

}
