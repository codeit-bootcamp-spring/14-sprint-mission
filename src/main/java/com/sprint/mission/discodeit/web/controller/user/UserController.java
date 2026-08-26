package com.sprint.mission.discodeit.web.controller.user;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.service.application.UserServiceApp;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.domain.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.web.controller.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserResponseDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserStatusResponseDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserUpdateResponseDTO;
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
    private final UserServiceApp userServiceApp;
    private final UserService userService;
    private final UserStatusService userStatusService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUserAccount(){
        List<UserResponseDTO> response = userServiceApp.findAllUser();

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUserAccount(
        @RequestPart(value = "userCreateRequest") UserCreateRequestDTO userCreateRequestDTO,
        @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ){
        UserResponseDTO response = userServiceApp.createUser(userCreateRequestDTO, profileImage);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable UUID userId){
        userServiceApp.deleteUserAccount(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserUpdateResponseDTO> updateUserAccount(
        @PathVariable UUID userId ,
        @RequestPart(value = "userUpdateRequest") UserUpdateRequestDTO userUpdateRequestDTO,
        @RequestPart(value = "profile") MultipartFile profileImage
    ){
        UserUpdateResponseDTO response = userServiceApp.updateUser(userId,
            userUpdateRequestDTO, profileImage);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    //todo: 온라인상태도 false 인데 바꿔야함 이게 여기있는게 맞나. 다시 해보셈
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDTO> activateUserStatus(@PathVariable UUID userId, @RequestBody
        UserStatusUpdateRequestDTO userStatusUpdateRequestDTO){
        UserStatus updatedUserStatus = userStatusService.updateUserStatusByUserId(userId,
            userStatusUpdateRequestDTO.newLastActiveAt());

        UserStatusResponseDTO response = UserStatusResponseDTO.from(updatedUserStatus);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
