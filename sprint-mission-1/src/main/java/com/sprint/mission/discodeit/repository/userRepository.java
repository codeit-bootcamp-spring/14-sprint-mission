package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.userService;

public interface userRepository extends userService {
    User userCreate(String userName, String role);
    User findByUser(String userName);
    void userUpdate(String userName, String updateUserName);
    void userDelete(String userName);
}
