package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 조회 결과로 엔티티가 아니라 UserDto를 돌려줌. password를 빼고 온라인 상태를 합쳐야 해서...
 * 프로필 이미지는 선택이라 Optional로 받음.
 */
public interface UserService {

    UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileRequest);

    UserDto find(UUID userId);

    List<UserDto> findAll();

    UserDto update(UUID userId, UserUpdateRequest request, Optional<BinaryContentCreateRequest> profileRequest);

    void delete(UUID userId);
}
