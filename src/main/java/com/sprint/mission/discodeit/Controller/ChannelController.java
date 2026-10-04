package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.IService.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<ChannelResponseDto> createPublic(
        @RequestBody PublicChannelCreateRequestDto request
    ) {
        log.debug("public 채널 생성 API 호출: name={}", request.name());

        ChannelResponseDto response =  channelService.createPublic(request);

        log.info("public 채널 생성 API 응답: channelId={}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/private")
    public ResponseEntity<ChannelResponseDto> createPrivate(
        @RequestBody PrivateChannelCreateRequestDto request
    ) {
        log.debug("private 채널 생성 API 호출: participantCount={}", request.participantIds().size());

        ChannelResponseDto response = channelService.createPrivate(request);

        log.info("private 채널 생성 API 응답: channelId={}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{channelId}")
    public ResponseEntity<ChannelResponseDto> update(
        @PathVariable UUID channelId,
        @RequestBody ChannelUpdateRequestDto request
    ) {
        log.debug("채널 수정 API 호출: channelId={}", channelId);

        ChannelResponseDto response = channelService.update(channelId, request);

        log.info("채널 수정 API 응답: channelId={}", channelId);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(method = RequestMethod.DELETE, path = "/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        log.debug("채널 삭제 API 호출: channelId={}", channelId);

        channelService.delete(channelId);

        log.info("채널 삭제 API 응답: channelId={}", channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> findAllByUserId(
        @RequestParam UUID userId
    ) {
        List<ChannelResponseDto> response = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
