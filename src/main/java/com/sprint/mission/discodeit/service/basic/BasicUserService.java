package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;



    @Override
    public UserResponseDto create(UserCreateRequestDto userRequest, BinaryContentCreateRequestDto profileRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByName(userRequest.name())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        UUID profileId = null;

        if(profileRequest!= null){
            BinaryContent binaryContent = profileRequest.toEntity();
            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }
        User user = userRequest.toEntity(profileId);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);
    }

    @Override
    public UserResponseDto read(UUID id) {
        User user = userRepository.findById(id);
        if(Objects.isNull(user)){
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
        UserStatus userStatus = userStatusRepository.findByUserId(id)
            .orElseThrow(() -> new RuntimeException("유저 상태 정보가 없습니다."));
        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);

    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateRequestDto requestDto, BinaryContentCreateRequestDto profileRequest) {
        User user = userRepository.findById(id);
        if(Objects.isNull(user)) {
            throw new RuntimeException("존재하지 않는 유저 입니다");
        }

        user.update(requestDto.name(),requestDto.email());

        if (profileRequest != null)  {
            BinaryContent binaryContent = profileRequest.toEntity();
            binaryContentRepository.save(binaryContent);
            user.updateProfileId(binaryContent.getId());
        }

        userRepository.save(user);

        UserStatus userStatus = userStatusRepository.findByUserId(id)
            .orElseThrow(() -> new RuntimeException("유저 상태정보가 없습니다"));
        boolean online = userStatus.isOnline();

        return UserResponseDto.from(user, online);


    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id);
        if (Objects.isNull(user)) {
            throw new RuntimeException("존재하지 않는 유저입니다");
        }


        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        UserStatus userStatus = userStatusRepository.findByUserId(id)
            .orElseThrow(() ->new RuntimeException("유저 상태 정보가 없습니다"));
        userStatusRepository.deleteById(userStatus.getId());

        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> readAll() {
        return userRepository.findAll().stream()
            .map(user -> {UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("유저 상태 정보가 없습니다."));
                boolean online = userStatus.isOnline();
                return UserResponseDto.from(user, online);})
            .toList();
    }

}
