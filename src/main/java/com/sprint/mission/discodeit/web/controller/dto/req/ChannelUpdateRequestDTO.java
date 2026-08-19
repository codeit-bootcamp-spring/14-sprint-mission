package com.sprint.mission.discodeit.web.controller.dto.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelUpdateRequestDTO {
    private String channelName;
}
