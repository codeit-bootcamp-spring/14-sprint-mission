package com.sprint.mission.discodeit.domain.service.user;

import com.sprint.mission.discodeit.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User findById(UUID id);
    User updateUser(UUID id, String name);
    List<User> findAllUser();
    void deleteUser(UUID id);
    boolean existAllByIdList(List<UUID> idList);
    User findUserByEmail(String email);
}
