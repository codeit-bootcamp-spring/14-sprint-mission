package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.application.channel.ChannelApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
/**
현 * [X] 공개 채널을 생성할 수 있다.
 * [X] 비공개 채널을 생성할 수 있다.
 * [X] 공개 채널의 정보를 수정할 수 있다.
 * [X] 채널을 삭제할 수 있다.
 * [X] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
 */
public class ChannelApiController {

    private final ChannelApplicationService channelApplicationService;

    @PostMapping("/api/channels")
    public ChannelResponseDto create(
            @Valid @RequestBody PublicChannelCreateRequestDto request
    ) {
        return channelApplicationService.createPublic(request);
    }

    @PostMapping("/api/channels/private")
    public ChannelResponseDto create(
            @Valid @RequestBody PrivateChannelCreateRequestDto request
    ) {
        return channelApplicationService.createPrivate(request);
    }

    @PutMapping("/api/channels/{id}")
    public ChannelResponseDto update(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody ChannelUpdateRequestDto request
    ) {
        return channelApplicationService.update(id, request);
    }

    @DeleteMapping("/api/channels/{id}")
    public void delete(@Valid @PathVariable UUID id) {
        channelApplicationService.delete(id);
    }

    @GetMapping("/api/channels/{id}")
    public List<ChannelResponseDto> retrieveAccessible(@Valid @PathVariable UUID id) {
        return channelApplicationService.findAllByUserId(id);
    }
}
