package com.sprint.mission.discodeit.web.controller.channel;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.service.application.ChannelServiceApp;
import com.sprint.mission.discodeit.domain.service.channel.ChannelService;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelPublicCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelPrivateCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ChannelFindResponseDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ChannelResponseDTO;
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
    private final ChannelServiceApp channelServiceApp;
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResponseDTO> makePublicChannel(@RequestBody ChannelPublicCreateRequestDTO channelPublicCreateRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelServiceApp.makePublicChannel(channelPublicCreateRequestDTO));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponseDTO> makePrivateChannel(@RequestBody ChannelPrivateCreateRequestDTO channelPrivateCreateRequestDTO){
        ChannelResponseDTO response = channelServiceApp.makePrivateChannel(channelPrivateCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id){
        channelServiceApp.deleteChannel(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    /*
        todo : ㅈㅁ

        명시된 api 규격에 따라 응답형태가 기존 채널 반환에서 디티오 형태로 반환이 됐는데
        간단한 요구사항이라 앱서비스가 아닌 컨트롤러 -> 도메인 엔터티 서비스를 호출하는 형태
        근데 이걸 그럼 컨트롤러에서 처리하라하면 내가 정한 규약인 컨트롤러는 바인딩과 빈검증 그리고 채널서비스호출과 응답만 처리.
        앱서비스는 디티오 조립 담당 규약에 어긋. 이걸 앱서비스에서  처리하는게 맞을까 컨트롤러에서 처리하는게 맞을지
        근데 앱서비스로 승격시키기엔 간단한 엔터티 상태 변경이라 고민
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ChannelResponseDTO> updateChannel(@PathVariable UUID id, @RequestBody ChannelUpdateRequestDTO channelUpdateRequestDTO){
        Channel updatedChannel = channelService.updateChannel(id, channelUpdateRequestDTO.newName(),
            channelUpdateRequestDTO.newDescription());
        ChannelResponseDTO response = ChannelResponseDTO.from(updatedChannel);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    /*
        todo : 추후 로그인 아이디로 변경
     */
    @GetMapping
    public ResponseEntity<List<ChannelResponseDTO>> findAllChannelByUserId(@RequestParam UUID userId){
        List<ChannelResponseDTO> response = channelServiceApp.findAllChannelByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ChannelFindResponseDTO> findPublicChannelInfo(@PathVariable UUID id){
        return ResponseEntity.ok(channelServiceApp.findChannel(id));
    }
}
