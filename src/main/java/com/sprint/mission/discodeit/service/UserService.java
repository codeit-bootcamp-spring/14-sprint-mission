package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성
    User create(User user);

    // 생성(Overload)
//    User create(String username, String email, String password);

    // 단건 조회
    User findById(UUID id);

    // 전체 조회
    List<User> findAll();

    // 수정
    User update(UUID id, User user);

    // 수정(Overload)
//    User update(UUID id, String username, String email, String password);

    // 삭제
    void delete(UUID id);
}
