package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    /** 등록·수정 모두 "현재 상태를 기록한다"는 같은 일이라 한 메서드로 받음. */
    User save(User user);

    /** 없을 수 있다는 것을 타입에 드러낸다. 없을 때의 처리는 Service가 정함. */
    Optional<User> findById(UUID id);

    /** 로그인처럼 username으로 엔티티 자체가 필요할 때 쓴다. existsByUsername과 달리 값을 돌려준다. */
    Optional<User> findByUsername(String username);

    List<User> findAll();

    void deleteById(UUID id);

    // 있는지만 알면 되는데 findAll()로 전부 꺼내오면 데이터가 커질수록 손해.
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
