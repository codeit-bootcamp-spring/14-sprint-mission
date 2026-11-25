package com.sprint.mission.discodeit.web.controller.readstatus;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.service.application.ReadStatusServiceApp;
import com.sprint.mission.discodeit.domain.service.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.web.controller.dto.req.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ReadStatusResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
    todo : 리드 스테이터스 api 는 어떨때 호출 될지
    채널 앱 서비스에 채널입장 메서드가 없는건가

    나 -> 리드스테이터스는 메인 엔터티로 존재하는게 아닌 다른 엔터티와의 조합으로 생성된다.
    채널 앱 서비스에서 입장 메서드를 통해 리드스테이터스가 생성되고 업데이트된다.

    요구사항 -> 리드스테이터스로 채널 입장을 관리하게 되는 독립 엔터티이다.
    따라서 리드스테이터스 api 가 존재해야하고 앱서비스도 필요하다.

    그럼 채널 관리자 이런게 존재하지 않는건가?
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusServiceApp readStatusServiceApp;
    private final ReadStatusService readStatusService;

    @GetMapping
    public ResponseEntity<List<ReadStatusResponseDTO>> findReadStatusesByUserId(@RequestParam UUID userId){
        List<ReadStatus> userReadStatuses = readStatusService.findReadStatusByUserId(userId);
        List<ReadStatusResponseDTO> response = ReadStatusResponseDTO.fromList(
            userReadStatuses);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    public ResponseEntity<ReadStatusResponseDTO> createReadStatus(@RequestBody ReadStatusCreateRequestDTO readStatusCreateRequestDTO){
        ReadStatusResponseDTO response = readStatusServiceApp.createReadStatus(
            readStatusCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDTO> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequestDTO readStatusUpdateRequestDTO){
        ReadStatus updatedReadStatus = readStatusService.updateReadStatusReadTime(readStatusId,
            readStatusUpdateRequestDTO.newLastReadAt());
        ReadStatusResponseDTO response = ReadStatusResponseDTO.from(updatedReadStatus);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
