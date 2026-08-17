package com.sprint.mission.discodeit.repository.jcf;

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
        User user = data.get(id);
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
        User targetUser = findById(id);

        data.remove(targetUser.getId());
        log.debug("JCF 사용자 데이터 삭제 : id={}", id);
    }
}
