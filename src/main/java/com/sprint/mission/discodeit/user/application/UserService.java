package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.common.application.BasicService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends BasicService<UserResponseDto> {
    UserResponseDto create(UserCreateRequestDto userRequestDto, MultipartFile profile);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    UserResponseDto find(UUID id);
    List<UserResponseDto> findAll();


    UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto, MultipartFile profile);
}
