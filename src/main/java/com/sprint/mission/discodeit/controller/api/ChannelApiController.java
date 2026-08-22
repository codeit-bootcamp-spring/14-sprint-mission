package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelApiController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/channel/public")
    public ResponseEntity<ChannelResponse> createPublic(@RequestBody PublicChannelCreate request) {
        ChannelResponse channelResponse = channelService.createPublic(request);
        return ResponseEntity.ok(channelResponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/channel/private")
    public ResponseEntity<ChannelResponse> createPrivate(@RequestBody PrivateChannelCreate request) {
        ChannelResponse response = channelService.createPrivate(request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/channel/{id}")
    public ResponseEntity<ChannelResponse> updateChannel(@PathVariable UUID id, @RequestBody ChannelRequest request) {
        ChannelResponse response = channelService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/channel/{id}")
    public void deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/channel/{userId}")
    public ResponseEntity<List<ChannelResponse>> findByUserId(@PathVariable UUID userId) {
        List<ChannelResponse> list = channelService.findByUserId(userId);
        return ResponseEntity.ok(list);
    }
}
