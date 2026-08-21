package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<User> create(
            @ModelAttribute UserCreateRequest request,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        Optional<BinaryContentCreateRequest> profileRequest = convertToBinaryRequest(profileImage);
        User user = userService.create(request, profileRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable UUID userId, @ModelAttribute UserUpdateRequest request,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage){

        Optional<BinaryContentCreateRequest> profileRequest = convertToBinaryRequest(profileImage);
        User user = userService.update(userId, request, profileRequest);
        return  ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserStatus> updateStutus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request){
        UserStatus userStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(userStatus);
    }

    // 공통 파일 변환 메서드 (컨트롤러 내부에 선언)
    private Optional<BinaryContentCreateRequest> convertToBinaryRequest(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                return Optional.of(new BinaryContentCreateRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
        }
        return Optional.empty();
    }
}
