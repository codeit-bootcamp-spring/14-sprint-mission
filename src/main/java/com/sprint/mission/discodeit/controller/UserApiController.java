package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.common.multipart.MultiPartFileUtil;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserStatusDto;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.application.UserApplication;
import com.sprint.mission.discodeit.application.UserStatusApplication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequestMapping(value = "/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApplication userApplication;
    private final UserStatusApplication userStatusApplication;
    private final MultiPartFileUtil multiPartFileUtil;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@Valid @RequestPart(required = true) UserCreateRequest userCreateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        CreateBinaryContentCommand createProfileCommand = createCommandIfNotNullOrElseGetNull(profile);
        return userApplication.createAccount(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password(),
                createProfileCommand
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}")
    public UserDto update(@PathVariable UUID userId,
                          @Valid @RequestPart UserUpdateRequest userUpdateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        CreateBinaryContentCommand createProfileCommand = createCommandIfNotNullOrElseGetNull(profile);
        return userApplication.updateUser(
                userId,
                userUpdateRequest.newUsername(),
                userUpdateRequest.newEmail(),
                userUpdateRequest.newPassword(),
                createProfileCommand
        );
    }

    /*
    TODO
    MultiPartFileUtil은 파일이 없거나 내용이 없으면 exception을 던지도록 설계함.
    파일이 있어야 하든 없어야 하든 요구사항과 관계 없이 일단 예상하지 못헌 비정상적인 요청에 대비한 합리적인 설계라고 생각

    근데 문제는 discodeit의 비즈니스 규칙을 보면 모든 생성,수정 시 binaryContent는 선택적으로 포함할 수 있도록 함
    파일이 null이건 아니건 정상 요청으로 간주하도록 util을 설계하는건 찝찝한데
    그렇다고 매번 중복되는 로직을 컨트롤러(UserApiController, MessageApiController)에 적는것도 비효율적이라고 생각.
     */
    private CreateBinaryContentCommand createCommandIfNotNullOrElseGetNull(MultipartFile file) {
        return Objects.nonNull(file) ?
                multiPartFileUtil.convert(file) : null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void delete(@PathVariable UUID userId) {
        userApplication.deleteAccount(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userApplication.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserDto read(@PathVariable UUID id) {
        return userApplication.getUser(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/userStatus")
    public UserStatusDto updateUserStatusByUserId(@PathVariable UUID userId,
                                                  @Valid @RequestBody UserStatusUpdateRequest request) {
        return userStatusApplication.updateByUserId(userId, request.newLastActiveAt());
    }
}
