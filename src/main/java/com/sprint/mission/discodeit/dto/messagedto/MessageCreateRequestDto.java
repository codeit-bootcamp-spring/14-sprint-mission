package com.sprint.mission.discodeit.dto.messagedto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {
    private String values;
    private UUID channelId;
    private UUID senderId;
    private List<BinaryContentCreateRequestDto> attachmentDtos;

    public MessageCreateRequestDto(String values, UUID channelId, UUID senderId) {
        this(values, channelId, senderId, null);
    }

    public Message toEntity() {
        return new Message(this.values, this.channelId, this.senderId);
    }
}
