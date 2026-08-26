package com.sprint.mission.discodeit.dto.messagedto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    /* 서비스 코드, 필드변경 없이 하려고 setter 메서드 추가,
    프론트에서 쏴주는거 매핑해주려고
    */
    public void setContent(String content) {
        this.values = content;
    }

    public String getContent() {
        return this.values;
    }

    public void setAuthorId(UUID authorId) {
        this.senderId = authorId;
    }

    public UUID getAuthorId() {
        return this.senderId;
    }

    public Message toEntity() {
        return new Message(this.values, this.channelId, this.senderId);
    }
}
