package com.sprint.mission.discodeit.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
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
public class PrivateChannelCreateRequestDto {
    private List<UUID> memberIds;

    /* 서비스 코드, 필드변경 없이 하려고 setter 메서드 추가,
    프론트에서 쏴주는거 매핑해주려고
     */
    public void setParticipantIds(List<UUID> participantIds) {
        this.memberIds = participantIds;
    }

    public List<UUID> getParticipantIds() {
        return this.memberIds;
    }

    public Channel toEntity() {
        return new Channel(null, this.memberIds, ChannelType.PRIVATE);
    }
}
