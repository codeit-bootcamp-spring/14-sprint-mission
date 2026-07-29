package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    UserRepository userRepository;

    public BasicUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        userRepository.save(user);
        return user;
    }

    @Override
    public User read(UUID id) {

        return check(id);
    }

    @Override
    public void update(User t) {
        User user1 = check(t.getId());
        user1.update(t.getUserName(), t.getEmail(), t.getPassword());
        userRepository.save(user1);
    }

    @Override
    public void delete(UUID id) {
        check(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> readAll() {
        return  userRepository.findAll();
    }


    private User check(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("해당 값이 존재하지 않습니다.");
        }
        return user;
    }
}
