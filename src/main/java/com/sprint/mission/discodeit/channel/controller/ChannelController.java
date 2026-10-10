package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.application.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    // public, private 채널 가르는건 service에서 함
    @PostMapping(value = "/public")
    public ChannelDto publicChannelCreate(@Valid @RequestBody PublicChannelCreateRequest request) {
        log.debug("public 채널 생성 - name : {}", request.name());
        return channelService.publicCreate(request);
    }

    @PostMapping(value = "/private")
    public ChannelDto privateChannelCreate(@Valid @RequestBody PrivateChannelCreateRequest request) {
        log.debug("private 채널 생성 - request : {}", request);
        return channelService.privateCreate(request);
    }

    @PatchMapping("/{channelId}")
    public ChannelDto updatePublicChannel(@PathVariable UUID channelId,
                                          @Valid @RequestBody ChannelUpdateRequestDto request) {
        log.debug("채널 수정 요청 - channelId : {}", channelId);
        return channelService.update(channelId, request);
    }

    @DeleteMapping("/{channelId}")
    public void delete(@PathVariable UUID channelId) {
        log.debug("채널 삭제 요청 - channelId : {}", channelId);
        channelService.delete(channelId);
    }

    @GetMapping
    public List<ChannelDto> findAllByUserId(@RequestParam UUID userId) {
        log.debug("사용자별 채널 목록 조회 요청 - userId : {}", userId);
        return channelService.findAllByUserId(userId);
    }


}
