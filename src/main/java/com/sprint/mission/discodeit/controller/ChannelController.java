package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
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

    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(
        @Valid @RequestBody PublicChannelCreateRequest request) {
        log.info("createPublic 요청. name:{}", request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublic(request));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(
        @Valid @RequestBody PrivateChannelCreateRequest request) {
        log.info("createPrivate 요청. participantIds:{}", request.participantIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivate(request));
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(
        @PathVariable UUID channelId,
        @Valid @RequestBody PublicChannelUpdateRequest request) {
        log.info("update 요청. channelId:{}", channelId);
        return ResponseEntity.ok(channelService.update(channelId, request));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        log.info("delete 요청. channelId:{}", channelId);
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAllByUserId(@RequestParam UUID userId) {
        log.info("findAllByUserId 요청. userId:{}", userId);
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
