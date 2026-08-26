package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping(value = "/public")
    public ResponseEntity<ChannelDto> publicChannelCreate(
        @Valid @RequestBody PublicChannelCreateRequest request
    ) {
        log.info("publicChannelCreate 정상 작동. 채널 이름:{}", request.name());
        ChannelDto created = channelService.createPublic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> privateChannelCreate(
        @Valid @RequestBody PrivateChannelCreateRequest request
    ) {
        log.info("privateChannelCreate 정상 작동.");
        ChannelDto created = channelService.createPrivate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(
        @PathVariable UUID channelId,
        @Valid @RequestBody ChannelUpdateRequest request
    ) {
        log.info("공개채널 수정 정상 작동.");
        ChannelDto updated = channelService.update(channelId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{channelId}")
    public void delete(@PathVariable UUID channelId) {
        log.info("delete 정상 작동. 삭제할 channelId:{}", channelId);
        channelService.delete(channelId);
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllByUserId(
        @RequestParam UUID userId) {
        log.info("findAllByUserId 정상 작동. userId:{}", userId);
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }


}
