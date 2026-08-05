package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    //메모리 업데이트 반환값이 필요없다고 생각함
    void save(UserStatus userStatus);

    //메모리에서 id로 유저상태 읽어오기
    UserStatus findById(UUID id);
    //userid로 유저가 해당채널에서 유저상태를 가져옴
    UserStatus findByUserId(UUID userId);

    //유저상태 목록 가져오기
    List<UserStatus> findAll();

    //메모리에서 유저상태 지우기
    void delete(UUID id);
}
