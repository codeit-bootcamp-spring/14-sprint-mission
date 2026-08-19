package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channels")
public class ChannelApiController {
    private final BasicChannelService channelService;

    // 1. 공개 채널을 생성할 수 있다.
    // 2. 비공개 채널을 생성할 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ChannelResponseDto createPublic(@Valid @RequestBody ChannelCreationDto request) {
        return channelService.createChannel(
                request.getChannelType(),
                request.getName(),
                request.getUserIds()
        );
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ChannelResponseDto updateChannelName(@PathVariable UUID id,
                                                @Valid @RequestBody ChannelUpdateNameDto request) {
        return channelService.updateChannelName(
                id,
                request.getName()
        );
    }


}
