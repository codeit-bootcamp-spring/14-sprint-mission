package com.sprint.mission.controller.api;

import com.sprint.mission.application.channel.ChannelApplicationService;
import com.sprint.mission.controller.dto.channel.ChannelDto;
import com.sprint.mission.controller.dto.channel.ChannelResponseDto;
import com.sprint.mission.controller.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.controller.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.controller.dto.channel.PublicChannelUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @PostMapping("/public")
    public ResponseEntity<ChannelResponseDto> create(
            @Valid @RequestBody PublicChannelCreateRequest request
    ) {
        ChannelResponseDto createdChannel = channelApplicationService.createPublic(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponseDto> create(
            @Valid @RequestBody PrivateChannelCreateRequest request
    ) {
        ChannelResponseDto createdChannel = channelApplicationService.createPrivate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResponseDto> update(
            @NotNull @PathVariable UUID channelId,
            @Valid @RequestBody PublicChannelUpdateRequest request
    ) {
        ChannelResponseDto updatedChannel = channelApplicationService.update(channelId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedChannel);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(
            @NotNull @PathVariable UUID channelId
    ) {
        channelApplicationService.delete(channelId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(
            @NotNull @RequestParam UUID userId
    ) {
        List<ChannelDto> channels = channelApplicationService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }
}
