package com.sprint.mission.discodeit.adaptor.api;

import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.channel.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelApiController {

  private final ChannelService channelService;

  @RequestMapping(method = RequestMethod.POST, value = "/public")
  public ResponseEntity<ChannelResponse> createPublic(
      @RequestBody PublicChannelCreateRequest request) {
    ChannelResponse channelResponse = channelService.createPublic(request);
    return ResponseEntity.status(201).body(channelResponse);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/private")
  public ResponseEntity<ChannelResponse> createPrivate(
      @RequestBody PrivateChannelCreateRequest request) {
    ChannelResponse channelResponse = channelService.createPrivate(request);
    return ResponseEntity.status(201).body(channelResponse);
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{channelId}")
  public ResponseEntity<ChannelResponse> updateChannel(@PathVariable UUID channelId,
      @RequestBody ChannelRequest request) {
    ChannelResponse response = channelService.update(channelId, request);
    return ResponseEntity.ok(response);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<List<ChannelDto>> findByUserId(
      @RequestParam(name = "userId") UUID userId) {
    List<ChannelDto> list = channelService.findByUserId(userId);
    return ResponseEntity.ok(list);
  }
}
