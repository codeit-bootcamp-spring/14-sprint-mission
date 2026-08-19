package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResponse;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponse userCreate(UserCreateRequestDto userCreateRequestDto,
        MultipartFile profile);

    UserResponse userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto,
        MultipartFile profile);

    void userDelete(UUID userId);

    List<UserDto> findAll();

    UserDto findById(UUID userId);
}
