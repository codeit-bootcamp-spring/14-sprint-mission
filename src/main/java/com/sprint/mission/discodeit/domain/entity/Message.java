package com.sprint.mission.discodeit.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Message implements Serializable, IdMapper {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    UUID id = UUID.randomUUID();
    UUID userId;
    UUID channelId;
    String content;

    @Builder.Default
    List<UUID> imageList = new ArrayList<>();
    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();

    static public Message init(UUID userId, UUID channelId, String content){
        return Message.builder()
            .userId(userId).channelId(channelId).content(content).build();
    }


    public void updateContent(String content){
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void updateMessageImagesFiled(List<UUID> imageList){
        this.imageList = imageList;
    }

    public boolean hasImageList(){
        return !imageList.isEmpty();
    }
}
