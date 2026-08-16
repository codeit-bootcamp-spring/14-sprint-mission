package com.sprint.mission.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        log.debug("JCF 사용자 데이터 저장 : id={}", user.getId());

        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        log.debug("JCF 사용자 데이터 조회 : id={}", id);

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = data.values()
                .stream()
                .toList();
        log.debug("JCF 사용자 전체 조회 : count={}", users.size());

        return users;
    }

    @Override
    public void delete(UUID id) {
        User targetUser = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 없습니다."));

        data.remove(targetUser.getId());
        log.debug("JCF 사용자 데이터 삭제 : id={}", id);
    }
}
