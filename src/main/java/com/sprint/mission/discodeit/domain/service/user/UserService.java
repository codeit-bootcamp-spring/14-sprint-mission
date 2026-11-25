package com.sprint.mission.discodeit.domain.service.user;

import com.sprint.mission.discodeit.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User findUserById(UUID id);
    User updateUser(UUID id, String name);
    User updateUser(UUID id, String name, String email, String password, UUID profileImageId);
    List<User> findAllUser();
    void deleteUser(UUID id);
    boolean existAllByIdList(List<UUID> idList);
    User findUserByEmail(String email);
}
