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

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Override
    public User createAccount(UserCreationDto dto) {
        // TODO 선택적으로 프로필 이미지를 등록할 수 있어야 한다.
        // 다른 필드들은 필수로 받아야한다는 뜻?
        // Controller 안거쳐서 @Valid는 의미 없으니 service에서 노가다
        if (Objects.isNull(dto.getName())) {
            throw new IllegalArgumentException("유저 생성 시 name이 꼭 필요합니다.");
        }
        if (Objects.isNull(dto.getEmail())) {
            throw new IllegalArgumentException("유저 생성 시 email이 꼭 필요합니다.");
        }
        if (Objects.isNull(dto.getPassword())) {
            throw new IllegalArgumentException("유저 생성 시 password가 꼭 필요합니다.");
        }

        // TODO username과 email은 다른 유저와 달라야 한다.
        // TODO UserStatus를 같이 생성해야 한다.
        User user = dto.toUser();
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
