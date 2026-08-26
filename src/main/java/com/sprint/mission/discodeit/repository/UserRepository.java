package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;

public interface UserRepository extends Repository<User> {


    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);

}
