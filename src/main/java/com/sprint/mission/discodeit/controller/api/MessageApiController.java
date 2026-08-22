package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageApiController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/message")
    public ResponseEntity<MessageResponse> createMessage(MessageCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/messages/{id}")
    public List<ResponseEntity<MessageResponse>> getMessagesByChannel(@PathVariable UUID id) {
        return messageService.findByChannel(id).stream()
            .map(ResponseEntity::ok).toList();
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/message/{id}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable UUID id,
        @RequestBody MessageRequest request) {
        if (id.equals(request.uuid())) {
        return ResponseEntity.ok(messageService.update(request));
        }
        return ResponseEntity.badRequest().build();
    }
}
