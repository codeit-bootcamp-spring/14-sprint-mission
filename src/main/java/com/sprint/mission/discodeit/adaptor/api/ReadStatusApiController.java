package com.sprint.mission.discodeit.adaptor.api;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusModifier;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusRegister;
import com.sprint.mission.discodeit.application.readstatus.provided.query.ReadStatusFinder;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusApiController {

  private final ReadStatusRegister readStatusRegister;
  private final ReadStatusModifier readStatusModifier;
  private final ReadStatusFinder readStatusFinder;

  @RequestMapping(method = RequestMethod.POST, value = "")
  public ResponseEntity<ReadStatusDto> createReadStatusByChannel(
      @RequestBody ReadStatusCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusRegister.register(request));
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatusByChannel(
      @PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest request) {

    ReadStatusDto update = readStatusModifier.modify(readStatusId, request);

    return ResponseEntity.ok(update);


  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<List<ReadStatusDto>> getReadStatusByUser(
      @RequestParam(name = "userId") UUID userId) {
    return ResponseEntity.ok(readStatusFinder.getByUserId(userId));
  }
}
