package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.*;
import com.sprint.mission.discodeit.channel.application.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    // public, private 채널 가르는건 service에서 함
    @PostMapping(value = "/public")
    public ChannelDto publicChannelCreate(@RequestBody PublicChannelCreateRequest request){
        return channelService.publicCreate(request);
    }

    @PostMapping(value = "/private")
    public ChannelDto privateChannelCreate(@RequestBody PrivateChannelCreateRequest request){
        return channelService.privateCreate(request);
    }

    @PatchMapping("/{channelId}")
    public ChannelDto updatePublicChannel(@PathVariable UUID channelId,
            @RequestBody ChannelUpdateRequestDto request){
        return channelService.update(channelId, request);
    }

    @DeleteMapping("/{channelId}")
    public void delete(@PathVariable UUID channelId){
        channelService.delete(channelId);
    }

    @GetMapping
    public List<ChannelDto> findAllByUserId(@RequestParam UUID userId){
        return channelService.findAllByUserId(userId);
    }




}
