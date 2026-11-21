package com.sprint.mission.discodeit.channel.web;

import com.sprint.mission.discodeit.channel.application.ChannelApplicationService;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelPublicCreateRequestDTO;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelUpdateRequestDTO;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelPrivateCreateRequestDTO;
import com.sprint.mission.discodeit.channel.web.dto.res.ChannelResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelApplicationService channelApplicationService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResponseDTO> makePublicChannel(
        @Valid @RequestBody ChannelPublicCreateRequestDTO request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelApplicationService.makePublicChannel(request));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponseDTO> makePrivateChannel(
        @Valid @RequestBody ChannelPrivateCreateRequestDTO request
    ){
        ChannelResponseDTO response = channelApplicationService.makePrivateChannel(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id){
        channelApplicationService.deleteChannel(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChannelResponseDTO> updateChannel(
        @PathVariable UUID id,
        @Valid @RequestBody ChannelUpdateRequestDTO request
    ){
        ChannelResponseDTO response = channelApplicationService.updateChannel(id, request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponseDTO>> findAllChannelByUserId(@RequestParam UUID userId){
        List<ChannelResponseDTO> response = channelApplicationService.findAllChannelByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
