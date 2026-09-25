package com.sprint.mission.discodeit.adaptor.api;

import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.application.message.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageApiController {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, value = "")
  public ResponseEntity<MessageResponse> createMessage(
      @RequestBody MessageCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request));
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<List<MessageResponse>> getMessagesByChannel(
      @RequestParam UUID channelId) {
    return ResponseEntity.ok(messageService.findByChannel(channelId).stream().toList());
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
  public ResponseEntity<MessageResponse> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageRequest request) {
    return ResponseEntity.ok(messageService.update(messageId, request));
  }
}
