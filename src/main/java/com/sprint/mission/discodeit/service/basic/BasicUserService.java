package com.sprint.mission.discodeit.service.basic;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {
    //아래 두줄의 코드가 의존성 주입하는 패턴임
    //여기서 메인코드에서 File타입으로 받냐, JCF타입으로 받냐에 따라 아래의 메서드들이 완전히 바뀜
    private final UserRepository userRepository;
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User createUser(String email, String name) {
        User user = new User(email, name);
        /* 여기서 save()메서드가 어떻게 뭘 하는지 관심없음 그냥 인터페이스에
        정의 되어있으니까 호출가능하고 타입에 따라 알아서 오버라이딩되니까 호출함 */
        userRepository.save(user);
        return user;
    }

    @Override
    public User readUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> readAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String name) {
        User target = userRepository.findById(id);
        target.setName(name);
        target.setUpdatedAt();
        userRepository.save(target);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id);
    }
}
