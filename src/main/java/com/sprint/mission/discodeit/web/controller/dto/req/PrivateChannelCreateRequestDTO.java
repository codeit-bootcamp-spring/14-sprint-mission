package com.sprint.mission.discodeit.web.controller.dto.req;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class PrivateChannelCreateRequestDTO {
    String channelName;
    List<UUID> userList;
}
