package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> create(@RequestBody MessageCreateRequest request){
        Message message = messageService.create(request, Collections.emptyList());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/channels/{channelId}")
    public ResponseEntity<List<Message>> findAllByChannelId(@PathVariable UUID channelId){
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @PutMapping("/channels/{messageId}")
    public ResponseEntity<Message> update(@PathVariable UUID messageId, @RequestBody
            MessageUpdateRequest request){
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    @DeleteMapping("/channels/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId){
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

}
