package com.sprint.mission.discodeit.domain.service.application;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.service.channel.ChannelService;
import com.sprint.mission.discodeit.domain.service.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.web.controller.dto.req.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ReadStatusResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadStatusServiceApp {
    private final ReadStatusService readStatusService;
    private final UserService userService;
    private final ChannelService channelService;

    public ReadStatusResponseDTO createReadStatus(ReadStatusCreateRequestDTO readStatusCreateRequestDTO){
        ReadStatus readStatus = ReadStatus.init(readStatusCreateRequestDTO.userId(),
            readStatusCreateRequestDTO.channelId(), readStatusCreateRequestDTO.lastReadAt());

        userService.findUserById(readStatus.getUserId());
        channelService.findChannelById(readStatus.getChannelId());
        ReadStatus createdReadStatus = readStatusService.createReadStatus(readStatus);

        return ReadStatusResponseDTO.from(createdReadStatus);
    }
}
