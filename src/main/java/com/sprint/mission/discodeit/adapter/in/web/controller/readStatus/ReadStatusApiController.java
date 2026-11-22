package com.sprint.mission.discodeit.adapter.in.web.controller.readStatus;

import com.sprint.mission.discodeit.adapter.in.web.controller.readStatus.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.adapter.in.web.controller.readStatus.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.adapter.in.web.controller.readStatus.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.core.application.readstatus.port.in.ReadStatusUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/readStatuses")
public class ReadStatusApiController {
    private final ReadStatusUseCase useCase;

    // 1. 특정 채널의 메세지 수신 정보를 생성할 수 있다.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReadStatusResponse createReadStatus(@Valid @RequestBody ReadStatusCreateDto request) {
        return ReadStatusResponse.from(useCase.create(
                request.userId(),
                request.channelId(),
                request.lastReadAt()
        ));
    }

    // 2. 특정 채널의 메시지 수신 정보를 수정할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{publicReadStatusId}")
    public ReadStatusResponse updateReadStatus(@PathVariable UUID publicReadStatusId,
                                               @Valid @RequestBody ReadStatusUpdateDto request) {
        return ReadStatusResponse.from(useCase.updateReadStatus(publicReadStatusId, request.newLastReadAt()));
    }

    // 3.  특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReadStatusResponse> getUserReadStatus(@RequestParam UUID userId) {
        return useCase.getAllReadStatusByUserId(userId).stream()
                .map(ReadStatusResponse::from)
                .toList();
    }
}
