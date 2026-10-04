package com.sprint.mission.discodeit.channel.adapter.api;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.application.dto.ChannelRequest;
import com.sprint.mission.discodeit.channel.application.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.application.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelModifier;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRegister;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRemover;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelFinder;
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

  private final ChannelRegister channelRegister;
  private final ChannelModifier channelModifier;
  private final ChannelRemover channelRemover;
  private final ChannelFinder channelFinder;

  @RequestMapping(method = RequestMethod.POST, value = "/public")
  public ResponseEntity<ChannelDto> createPublic(
      @RequestBody PublicChannelCreateRequest request) {
    ChannelDto channelDto = channelRegister.registerPublic(request);
    return ResponseEntity.status(201).body(channelDto);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/private")
  public ResponseEntity<ChannelDto> createPrivate(
      @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto channelDto = channelRegister.registerPrivate(request);
    return ResponseEntity.status(201).body(channelDto);
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId,
      @RequestBody ChannelRequest request) {
    ChannelDto response = channelModifier.modify(channelId, request);
    return ResponseEntity.ok(response);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelRemover.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<List<ChannelDto>> findByUserId(
      @RequestParam(name = "userId") UUID userId) {
    List<ChannelDto> list = channelFinder.getAllByUserId(userId);
    return ResponseEntity.ok(list);
  }
}
