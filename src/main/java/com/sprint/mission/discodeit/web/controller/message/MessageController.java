package com.sprint.mission.discodeit.web.controller.message;

import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.service.application.MessageServiceApp;
import com.sprint.mission.discodeit.domain.service.message.MessageService;
import com.sprint.mission.discodeit.web.controller.dto.req.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.MessageUpdateRequestDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    메시지 관리
        [ ] 메시지를 보낼 수 있다. -> ok
        [ ] 메시지를 수정할 수 있다. -> ok
        [ ] 메시지를 삭제할 수 있다. -> ok
        [ ] 특정 채널의 메시지 목록을 조회할 수 있다. ->

    todo : 바이너리파일 테스트하기
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageServiceApp messageServiceApp;
    private final MessageService messageService;

    //todo : 로그인 구현해서 유저아이디쓰기
    @PostMapping("/{channelId}")
    public ResponseEntity<Message> inputMessage(@PathVariable UUID channelId, @ModelAttribute CreateMessageRequestDTO createMessageRequestDTO){
        Message message = messageServiceApp.createMessage(channelId, createMessageRequestDTO);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Message> updateMessageContent(
        @PathVariable UUID id,
        @RequestBody MessageUpdateRequestDTO messageUpdateRequestDTO
    ){
        Message message = messageService.updateMessageContent(id, messageUpdateRequestDTO.getContent());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id){
        messageServiceApp.deleteMessage(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<Message>> findAllMessageByChannelId(@PathVariable UUID channelId){
        List<Message> allMessageByChannelId = messageService.findAllMessageByChannelId(channelId);

        return ResponseEntity.ok(allMessageByChannelId);
    }
}
