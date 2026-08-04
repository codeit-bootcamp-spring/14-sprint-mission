package com.sprint.mission.discodeit.dto.messagedto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {
    private String values;
    private UUID channelId;
    private UUID senderId;

    public Message toEntity() {
        return new Message(this.values, this.channelId, this.senderId);
    }
}
