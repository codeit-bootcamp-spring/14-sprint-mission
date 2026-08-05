package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;



    @Override
    public void create(User entity) {
        if(userRepository.findById(entity.getId()) != null){
            throw new RuntimeException("이미 존재하는 유저입니다");
        }
        userRepository.save(entity);
    }

    @Override
    public User read(UUID id) {
        User user = userRepository.findById(id);
        if(Objects.isNull(user)){
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
        return user;
    }

    @Override
    public void update(User user, String updatedname, String updatedemail) {
        read(user.getId());
        user.setName(updatedname);
        user.setEmail(updatedemail);
        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        read(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
