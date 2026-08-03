package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.entity.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Override
    public User createAccount(UserCreationDto dto) {
        // TODO 1. 선택적으로 프로필 이미지를 등록할 수 있어야 한다.
        // TODO 2. DTO를 활용해 파라미터 그룹화해야 한다.
        // TODO 3. username과 email은 다른 유저와 달라야 한다.
        // TODO 4. UserStatus를 같이 생성해야 한다.
        User user = new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getProfileId());
        return userRepository.create(user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        // TODO 1. 사용자 온라인 정보를 포함시켜야 한다.
        // TODO 2. 패스워드 정보는 제외해야 한다.
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        // TODO 1. 사용자 온라인 정보를 포함시켜야 한다.
        // TODO 2. 패스워드 정보는 제외해야 한다.
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto dto) {
        // TODO 1. 선택적으로 프로필 이미지를 대체할 수 있어야 한다.
        // TODO 2. DTO를 활용해 파라미터를 그룹화한다.
        userRepository.updateName(id, dto.getName());
    }

    @Override
    public void deleteAccount(UUID id) {
        // TODO 1. 관련된 도메인도 같이 삭제한다. (BinaryContent, UserStatus, ReadStatus ... )
        channelRepository.deleteUsersByUserId(id);
        messageRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
