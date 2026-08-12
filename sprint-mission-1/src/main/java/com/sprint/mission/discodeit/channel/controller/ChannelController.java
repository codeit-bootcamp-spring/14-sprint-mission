package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChannelController {

    private final ChannelService channelService;

    // 공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/api/publicChannel")
    public ChannelResponseDto publicCreate(
        @Valid @RequestBody ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        return channelService.channelCreate(channelPublicCreateRequestDto);
    }

    // 비공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/api/privateChannel")
    public ChannelResponseDto privateCreate(
        @Valid @RequestBody ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        return channelService.privateChannelCreate(channelPrivateCreateRequestDto);
    }

    // 공개 채널 수정
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/channel/{id}")
    public void update(
        @PathVariable UUID id,
        @Valid @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto) {
        channelService.channelUpdate(id, channelUpdateRequestDto);
    }

    // 채널 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/channel/{id}/delete")
    public void delete(
        @PathVariable UUID id) {
        channelService.channelDelete(id);
    }

    // 특정 사용자의 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET, value = "/api/channel/{userId}/findAll")
    public List<ChannelResponseDto> findAllByUserId(
        @PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
