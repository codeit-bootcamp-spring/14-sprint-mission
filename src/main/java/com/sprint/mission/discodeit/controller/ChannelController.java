package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponseDto createPublic(@RequestBody ChannelCreateRequestDto dto) {
        return channelService.createPublicChannel(dto);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponseDto createPrivate(@RequestBody PrivateChannelCreateRequestDto dto) {
        return channelService.createPrivateChannel(dto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{channelId}")
    public ChannelResponseDto update(@PathVariable UUID channelId,
                                     @RequestBody ChannelUpdateRequestDto dto) {
        return channelService.updateChannel(channelId, dto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ChannelResponseDto> getChannelListByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
