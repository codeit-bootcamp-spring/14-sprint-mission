package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<ChannelResponseDto> createChannel(@RequestBody ChannelCreateRequestDto requestDto) {
        ChannelResponseDto response = channelService.createChannel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelResponseDto> readChannel(@PathVariable UUID id) {
        ChannelResponseDto response = channelService.readChannel(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponseDto>> readAllChannel() {
        List<ChannelResponseDto> responses = channelService.readAllChannel();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChannelResponseDto> updateChannel(@PathVariable UUID id, @RequestBody ChannelUpdateRequestDto requestDto) {
        ChannelResponseDto response = channelService.updateChannel(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }
}
