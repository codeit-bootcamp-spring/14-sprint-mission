package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // Create
    UserResponseDto createUser(UserCreateRequestDto requestDto);

    // Read (id를 받아 UserResponseDto 반환)
    UserResponseDto readUser(UUID id);

    // Read (전체 조회)
    List<UserResponseDto> readAllUser();

    // Update (유저 정보 업데이트)
    UserResponseDto updateUser(UUID id, UserUpdateRequestDto requestDto);

    // Delete (유저 정보 삭제)
    void deleteUser(UUID id);
}
