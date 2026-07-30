    package com.sprint.mission.discodeit.entity;

    import java.util.UUID;

    public class Channel {

        private final UUID id;
        private long createdAt;
        private long updatedAt;
        private String name;
        private int channelNum;

        public Channel(String name, int channelNum) {
            this.id = UUID.randomUUID();
            long now = System.currentTimeMillis();
            this.createdAt = now;
            this.updatedAt = now;
            this.name = name;
            this.channelNum = channelNum;
        }


        public UUID getId() {
            return id;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public String getName() {
            return name;
        }

        public int getChannelNum() {
            return channelNum;
        }


        public void update(String name, int channelNUm) {
            this.name = name;
            this.channelNum = channelNum;
            this.updatedAt = System.currentTimeMillis();
        }
    }