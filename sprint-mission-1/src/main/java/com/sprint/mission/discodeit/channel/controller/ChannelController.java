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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChannelController {

    private final ChannelService channelService;

    // 공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/api/channels/public")
    public ResponseEntity<ChannelResponseDto> publicCreate(
        @Valid @RequestBody ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channelService.channelCreate(channelPublicCreateRequestDto));
    }

    // 비공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/api/channels/private")
    public ResponseEntity<ChannelResponseDto> privateCreate(
        @Valid @RequestBody ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channelService.privateChannelCreate(channelPrivateCreateRequestDto));
    }

    // 공개 채널 수정
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/channels/{channelId}")
    public void update(
        @PathVariable UUID channelId,
        @Valid @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto) {
        channelService.channelUpdate(channelId, channelUpdateRequestDto);
    }

    // 채널 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/channels/{channelId}")
    public void delete(
        @PathVariable UUID channelId) {
        channelService.channelDelete(channelId);
    }

    // 특정 사용자의 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET, value = "/api/channels")
    public ResponseEntity<List<ChannelResponseDto>> findAllByUserId(
        @RequestParam UUID userId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.findAllByUserId(userId));
    }
}
