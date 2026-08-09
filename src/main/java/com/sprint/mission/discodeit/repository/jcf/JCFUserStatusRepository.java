package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    final List<UserStatus> userStatusList;

    public JCFUserStatusRepository() {
        this.userStatusList = new ArrayList<>();
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.add(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        for (UserStatus each : userStatusList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        for (UserStatus each : userStatusList) {
            if (each.getUserId() != null && each.getUserId().equals(userId)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public void delete(UUID id) {
        UserStatus target = findById(id);
        if (target != null) {
            userStatusList.remove(target);
        }
    }
}
