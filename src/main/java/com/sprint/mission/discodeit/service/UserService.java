package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService extends BasicService<User>{
    User create(String userName, String email, String password);

}
