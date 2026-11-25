package com.sprint.mission.discodeit.web.controller.message;

import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.service.application.MessageServiceApp;
import com.sprint.mission.discodeit.domain.service.message.MessageService;
import com.sprint.mission.discodeit.web.controller.dto.req.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.MessageResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageServiceApp messageServiceApp;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> findAllMessageByChannelId(@RequestParam UUID channelId){
        //todo : 지금 바로 메시지 서비스 호출해서 없는 채널 호출해도 빈값 리턴
        List<Message> allMessageByChannelId = messageService.findAllMessageByChannelId(channelId);
        List<MessageResponseDTO> response = MessageResponseDTO.fromList(
            allMessageByChannelId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    //todo : 로그인 구현해서 유저아이디쓰기
    @PostMapping
    public ResponseEntity<MessageResponseDTO> inputMessage(
        @RequestPart(value = "messageCreateRequest") MessageCreateRequestDTO messageCreateRequestDTO,
        @RequestPart(required = false) List<MultipartFile> attachments
    ){
        MessageResponseDTO response = messageServiceApp.createMessage(messageCreateRequestDTO, attachments);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageServiceApp.deleteMessage(messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponseDTO> updateMessageContent(
        @PathVariable UUID messageId,
        @RequestBody MessageUpdateRequestDTO messageUpdateRequestDTO
    ){
        Message updatedMessage = messageService.updateMessageContent(messageId, messageUpdateRequestDTO.getNewContent());
        MessageResponseDTO response = MessageResponseDTO.from(updatedMessage);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
