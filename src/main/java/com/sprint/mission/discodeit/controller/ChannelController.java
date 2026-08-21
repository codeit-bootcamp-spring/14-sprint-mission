package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ChannelResponseDto createPublic(@RequestBody ChannelCreateRequestDto dto) {
        return channelService.createPublicChannel(dto);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ChannelResponseDto createPrivate(@RequestBody PrivateChannelCreateRequestDto dto) {
        return channelService.createPrivateChannel(dto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ChannelResponseDto update(@PathVariable UUID id,
                                     @RequestBody ChannelUpdateRequestDto dto) {
        return channelService.updateChannel(id, dto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable UUID id) {
        channelService.deleteChannel(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ChannelResponseDto> getChannelListByUserId(@RequestParam UUID id) {
        return channelService.findAllByUserId(id);
    }
}
