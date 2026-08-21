package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channels")
public class ChannelApiController {
    private final BasicChannelService channelService;

    // 1. 공개 채널을 생성할 수 있다.
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ChannelResponseDto createPublicChannel(@Valid @RequestBody PublicChannelCreateDto request) {
        return channelService.createPublicChannel(
                request.name(),
                request.description()
        );
    }

    // TODO 유저 없어서 테스트 진행 불가. 유저 다 구현 후 여기부터 진행
    // 2. 비공개 채널을 생성할 수 있다.
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ChannelResponseDto createPrivateChannel(@Valid @RequestBody PrivateChannelCreateDto request) {
        return channelService.createPrivateChannel(
                request.participantIds()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{publicChannelId}")
    public ChannelResponseDto updateChannelName(@PathVariable UUID publicChannelId,
                                                @Valid @RequestBody ChannelUpdateNameDto request) {
        return channelService.updateChannelName(
                publicChannelId,
                request.newName(),
                request.newDescription()
        );
    }

    // 3. 채널을 삭제할 수 있다.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ChannelResponseDto deleteChannel(@PathVariable UUID id) {
        return channelService.deleteChannel(id);
    }

    // 4. 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelResponseDto> getAllChannels(@RequestParam UUID userId) {
        return channelService.getAllChannelsByUserId(userId);
    }

}
