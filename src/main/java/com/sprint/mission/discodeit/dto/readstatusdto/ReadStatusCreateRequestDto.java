package com.sprint.mission.discodeit.dto.readstatusdto;


import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusCreateRequestDto {
    private UUID userId;
    private UUID channelId;

    public ReadStatus toEntity() {
        return new ReadStatus(this.userId, this.channelId);
    }

}
