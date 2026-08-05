package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> createMessage(@RequestBody MessageCreateRequestDto requestDto) {
        MessageResponseDto response = messageService.createMessage(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDto> readMessage(@PathVariable UUID id) {
        MessageResponseDto response = messageService.readMessage(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> readAllMessage() {
        List<MessageResponseDto> responses = messageService.readAllMessage();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable UUID id, @RequestBody MessageUpdateRequestDto requestDto) {
        MessageResponseDto response = messageService.updateMessage(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
