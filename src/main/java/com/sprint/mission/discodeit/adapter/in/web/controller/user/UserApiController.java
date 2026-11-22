package com.sprint.mission.discodeit.adapter.in.web.controller.user;

import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.adapter.in.web.controller.multipart.MultiPartFileUtil;
import com.sprint.mission.discodeit.core.application.user.port.in.UserUseCase;
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
    private final UserUseCase useCase;
    private final MultiPartFileUtil multiPartFileUtil;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto create(@Valid @RequestPart(required = true) UserCreateRequest userCreateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        CreateBinaryContentCommand createProfileCommand = createCommandIfNotNullOrElseGetNull(profile);
        return UserDto.from(useCase.createAccount(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password(),
                createProfileCommand
        ));
    }

    @GetMapping(value = "/{id}")
    public UserDto get(@PathVariable UUID id) {
        return UserDto.from(useCase.getUser(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> get() {
        return useCase.getAllUsers().stream()
                .map(UserDto::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{userId}")
    public UserDto update(@PathVariable UUID userId,
                          @Valid @RequestPart UserUpdateRequest userUpdateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        CreateBinaryContentCommand createProfileCommand = createCommandIfNotNullOrElseGetNull(profile);
        return UserDto.from(useCase.patchUserInfo(
                userId,
                userUpdateRequest.newUsername(),
                userUpdateRequest.newEmail(),
                userUpdateRequest.newPassword(),
                createProfileCommand
        ));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable UUID userId) {
        useCase.deleteAccount(userId);
    }

    private CreateBinaryContentCommand createCommandIfNotNullOrElseGetNull(MultipartFile file) {
        return Objects.nonNull(file) ?
                multiPartFileUtil.convert(file) : null;
    }
}
