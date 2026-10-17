package com.sprint.mission.discodeit.user.web;

import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.web.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.req.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.req.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserStatusResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserApplicationService userApplicationService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUserAccount(){
        List<UserResponseDTO> response = userApplicationService.findAllUser();

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUserAccount(
        @Valid @RequestPart(value = "userCreateRequest") UserCreateRequestDTO request,
        @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ){
        UserResponseDTO response = userApplicationService.createAccount(request, profileImage);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable UUID userId){
        userApplicationService.deleteUserAccount(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUserAccount(
        @PathVariable UUID userId ,
        @Valid @RequestPart(value = "userUpdateRequest") UserUpdateRequestDTO request,
        @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ){
        UserResponseDTO response = userApplicationService.updateUser(userId, request, profileImage);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDTO> activateUserStatus(
        @PathVariable UUID userId,
        @Valid @RequestBody UserStatusUpdateRequestDTO request
    ){
        UserStatusResponseDTO response = userApplicationService.updateUserStatus(userId, request.newLastActiveAt());

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
