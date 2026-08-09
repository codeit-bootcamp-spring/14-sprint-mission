package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // < CRUD 기능 >
    // 유저 생성
    User createUser(String nickName, String email);
    // 유저 상세 조회
    User readUser(UUID id);
    // 유저 전체 조회
    List<User> readAllUsers();
    // 유저 수정
    User updateUser(UUID id, String nickName, String email);
    // 유저 삭제
    void deleteUser(UUID id);
}
