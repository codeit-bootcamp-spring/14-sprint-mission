package com.sprint.mission.controller.api;

import com.sprint.mission.dto.channel.ChannelResponseDto;
import com.sprint.mission.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.application.channel.ChannelApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
/**
 * [X] 공개 채널을 생성할 수 있다.
 * [X] 비공개 채널을 생성할 수 있다.
 * [X] 공개 채널의 정보를 수정할 수 있다.
 * [X] 채널을 삭제할 수 있다.
 * [X] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
 */
public class ChannelApiController {

    private final ChannelApplicationService channelApplicationService;

    @PostMapping
    public ResponseEntity<ChannelResponseDto> create(
            @Valid @RequestBody PublicChannelCreateRequestDto request
    ) {
        ChannelResponseDto createdPublicChannel = channelApplicationService.createPublic(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPublicChannel);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponseDto> create(
            @Valid @RequestBody PrivateChannelCreateRequestDto request
    ) {
        ChannelResponseDto createdPrivateChannel = channelApplicationService.createPrivate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPrivateChannel);
    }

    // patch로? <- 어떻게 달라질지
    @PutMapping
    public ResponseEntity<ChannelResponseDto> update(
            @Valid @RequestParam UUID channelId,
            @Valid @RequestBody ChannelUpdateRequestDto request
    ) {
        ChannelResponseDto updatedChannel = channelApplicationService.update(channelId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedChannel);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @Valid @RequestParam UUID channelId
    ) {
        channelApplicationService.delete(channelId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponseDto>> retrieveAccessible(
            @Valid @RequestParam UUID userId
    ) {
        List<ChannelResponseDto> accssibleChannelList = channelApplicationService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accssibleChannelList);
    }
}
