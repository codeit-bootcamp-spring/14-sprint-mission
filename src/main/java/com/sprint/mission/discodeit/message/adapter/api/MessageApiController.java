package com.sprint.mission.discodeit.message.adapter.api;

import com.sprint.mission.discodeit.binarycontent.BinaryContentRequestMapper;
import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.common.CursorPageResponse;
import com.sprint.mission.discodeit.message.application.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageCursorRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import com.sprint.mission.discodeit.message.application.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.application.provided.command.MessageModifier;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRegister;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRemover;
import com.sprint.mission.discodeit.message.application.provided.query.MessageFinder;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageApiController {

  private final MessageRegister messageRegister;
  private final MessageRemover messageRemover;
  private final MessageFinder messageFinder;
  private final MessageModifier messageModifier;
  private final BinaryContentRequestMapper binaryContentRequestMapper;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    List<BinaryContentCreateRequest> files = binaryContentRequestMapper.toCreateRequests(
        attachments);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageRegister.register(request, files));
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageRemover.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<CursorPageResponse<MessageDto>> getMessagesByChannel(
      @ModelAttribute MessageCursorRequest request) {
    return ResponseEntity.ok(messageFinder.getByCursor(request));
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    return ResponseEntity.ok(messageModifier.modify(messageId, request));
  }
}
