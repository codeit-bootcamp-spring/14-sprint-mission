package com.sprint.mission.discodeit.entity;


public class Channel extends BasicEntity {


    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setUpdatedAt(Long updatedAt) {
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
