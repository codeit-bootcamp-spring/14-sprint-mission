package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/read-status")
public class ReadStatusApiController {
    private final BasicReadStatusService readStatusService;

    // 1. 특정 채널의 메세지 수신 정보를 생성할 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusResponse createReadStatus(@Valid @RequestBody ReadStatusCreateDto request) {
        return readStatusService.create(
                request.getUserId(),
                request.getChannelId()
        );
    }

    // 2. 특정 채널의 메시지 수신 정보를 수정할 수 있다.
    @RequestMapping(method = RequestMethod.PATCH, value = "/{channelId}")
    public List<ReadStatusResponse> updateReadStatus(@PathVariable UUID channelId,
                                                     @Valid @RequestBody ReadStatusUpdateDto request) {
        return readStatusService.updateReadStatus(channelId);
    }

    // 3.  특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public List<ReadStatusResponse> getUserReadStatus(@PathVariable UUID userId) {
        return readStatusService.getAllReadStatusByUserId(userId);
    }

}
