package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.application.ChannelApplication;
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
    private final ChannelApplication channelApplication;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/public")
    public ChannelUpsertResponse createPublicChannel(@Valid @RequestBody PublicChannelCreateDto request) {
        return channelApplication.createPublicChannel(
                request.name(),
                request.description()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/private")
    public ChannelUpsertResponse createPrivateChannel(@Valid @RequestBody PrivateChannelCreateDto request) {
        return channelApplication.createPrivateChannel(
                request.participantIds()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{publicChannelId}")
    public ChannelUpsertResponse updateChannelName(@PathVariable UUID publicChannelId,
                                                @Valid @RequestBody ChannelUpdateNameDto request) {
        return channelApplication.updateChannelName(
                publicChannelId,
                request.newName(),
                request.newDescription()
        );
    }

    // 3. 채널을 삭제할 수 있다.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteChannel(@PathVariable UUID id) {
        channelApplication.deleteChannel(id);
    }

    // 4. 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ChannelResponseDto> getAllChannels(@RequestParam UUID userId) {
        return channelApplication.getAllChannelsByUserId(userId);
    }

}
