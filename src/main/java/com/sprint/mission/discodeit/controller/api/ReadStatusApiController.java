package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReadStatusApiController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/readstatus")
    public ResponseEntity<ReadStatusResponse> createReadStausByChannel(
        @RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/readstatus/channel/{id}")
    public ResponseEntity<ReadStatusResponse> updateReadStatusByChannel(@PathVariable UUID id,
        @RequestBody
        ReadStausRequest request) {
        UUID readStatusId = request.uuid();
        ReadStatusResponse response = readStatusService.findById(readStatusId);

            if (id.equals(response.ChannelId())) {
                return ResponseEntity.ok(readStatusService.update(request));
            }
        return ResponseEntity.badRequest().build();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/readstatus/user/{id}")
    public ResponseEntity<List<ReadStatusResponse>> getReadStatusByUser(@PathVariable UUID id) {
        return ResponseEntity.ok(readStatusService.findByUserId(id));
    }
}
