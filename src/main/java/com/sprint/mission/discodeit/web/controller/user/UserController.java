package com.sprint.mission.discodeit.web.controller.user;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.service.application.UserServiceApp;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.domain.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.web.controller.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserResponseDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserUpdateResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    사용자 관리
        [ ] 사용자를 등록할 수 있다.   -> ok
        [ ] 사용자 정보를 수정할 수 있다. -> ok
        [ ] 사용자를 삭제할 수 있다. -> ok
        [ ] 모든 사용자를 조회할 수 있다. -> ok
        [ ] 사용자의 온라인 상태를 업데이트할 수 있다. -> ok
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceApp userServiceApp;
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUserAccount(@ModelAttribute UserCreateRequestDTO userCreateRequestDTO){
        UserResponseDTO res = userServiceApp.createUser(userCreateRequestDTO);

        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> updateUserAccount(@PathVariable UUID id , @RequestBody UserUpdateRequestDTO userUpdateRequestDTO){
        User updatedUser = userService.updateUser(id, userUpdateRequestDTO.getName());
        UserUpdateResponseDTO res = UserUpdateResponseDTO.of(updatedUser);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable UUID id){
        userServiceApp.deleteUserAccount(id);

        return ResponseEntity.ok("success");
    }

    /*
        코드잇 심화 미션용
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUserAccount(){
        return ResponseEntity.ok(userServiceApp.findAllUser());
    }

    @GetMapping("/online/{id}")
    public ResponseEntity<UserStatus> activateUserStatus(@PathVariable UUID id){
        return ResponseEntity.ok(userStatusService.updateUserStatusByUserId(id));
    }
}
