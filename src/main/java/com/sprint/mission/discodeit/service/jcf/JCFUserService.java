package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class JCFUserService extends JCFBasicService<User> implements UserService {
   UserRepository userRepository;

    public JCFUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        userRepository.save(user);
        return user;
    }
}
