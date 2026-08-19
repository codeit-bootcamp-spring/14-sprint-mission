package com.sprint.mission.discodeit.web.controller.channel;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.service.application.ChannelServiceApp;
import com.sprint.mission.discodeit.domain.service.channel.ChannelService;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.PrivateChannelCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ChannelFindResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    채널 관리
        [ ] 공개 채널을 생성할 수 있다. -> ok
        [ ] 비공개 채널을 생성할 수 있다. -> ok
        [ ] 공개 채널의 정보를 수정할 수 있다. -> ok
        [ ] 채널을 삭제할 수 있다. -> ok
        [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다. -> ok
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelServiceApp channelServiceApp;
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<Channel> makePublicChannel(@RequestBody  ChannelCreateRequestDTO channelCreateRequestDTO){
        return ResponseEntity.ok(channelServiceApp.makePublicChannel(channelCreateRequestDTO));
    }

    @PostMapping("/private")
    public ResponseEntity<Channel> makePrivateChannel(@RequestBody PrivateChannelCreateRequestDTO privateChannelCreateRequestDTO){
        return ResponseEntity.ok(channelServiceApp.makePrivateChannel(privateChannelCreateRequestDTO));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ChannelFindResponseDTO> findPublicChannelInfo(@PathVariable UUID id){
        return ResponseEntity.ok(channelServiceApp.findChannel(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Channel> updateChannelName(@PathVariable UUID id, @RequestBody ChannelUpdateRequestDTO channelUpdateRequestDTO){
        return ResponseEntity.ok(channelService.updateChannelName(id, channelUpdateRequestDTO.getChannelName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID id){
        channelServiceApp.deleteChannel(id);
        return ResponseEntity.ok("success");
    }

    /*
        todo : 추후 로그인 아이디로 변경
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Channel>> findAllChannelByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(channelServiceApp.findAllChannelByUserId(userId));
    }
}
