package com.sprint.mission.discodeit.entity;


import java.time.Instant;
import lombok.Getter;

@Getter
public class Channel extends BasicEntity {


    private String name;

    public Channel(String name) {
        super();
        this.name = name;
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
