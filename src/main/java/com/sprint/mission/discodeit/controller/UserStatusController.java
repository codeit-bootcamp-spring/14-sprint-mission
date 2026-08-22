package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserStatusService;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userStatus")
public class UserStatusController {

    private final UserStatusService userStatusService;

    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @GetMapping("/find")
    public ResponseEntity<UserStatusDto> find(@RequestParam UUID userStatusId) {
        return ResponseEntity.ok(userStatusService.find(userStatusId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserStatusDto>> findAll() {
        return ResponseEntity.ok(userStatusService.findAll());
    }

    @PatchMapping("/update")
    public ResponseEntity<UserStatusDto> update(
            @RequestParam UUID userStatusId, @RequestBody UserStatusUpdateRequest request) {
        return ResponseEntity.ok(userStatusService.update(userStatusId, request));
    }

    @PatchMapping("/updateByUserId")
    public ResponseEntity<UserStatusDto> updateByUserId(
            @RequestParam UUID userId, @RequestBody UserStatusUpdateRequest request) {
        return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
    }
}
