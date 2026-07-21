package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService extends JCFService<User> implements UserService {


    @Override
    public void update(User user, String updatedname, String updatedemail) {
        User existing = read(user.getId());
        existing.setName(updatedname);
        existing.setEmail(updatedemail);
    }

}
