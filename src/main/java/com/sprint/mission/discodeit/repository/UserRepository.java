package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    // 저장(생성 및 수정)
    User save(User user);

    // 단건 조회
    User findById(UUID id);

    // 전체 조회
    List<User> findAll();

    // 삭제
    void delete(UUID id);
}
