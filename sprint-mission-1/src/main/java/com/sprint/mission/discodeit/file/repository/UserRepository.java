package com.sprint.mission.discodeit.file.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void userLoad();
    void userFlush();
    List<String> readUserName();
    void userAdd(List<User> users);
    Optional<User> findByUser(String userName);
    void delete(User user);
    List<User> findAllUser();
}
