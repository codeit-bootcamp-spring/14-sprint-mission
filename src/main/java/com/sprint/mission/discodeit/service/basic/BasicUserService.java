package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public BasicUserService(UserRepository userRepository,
                            UserStatusRepository userStatusRepository,
                            BinaryContentRepository binaryContentRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userCreateRequestDto.toEntity();
        if (userRepository.findByEmail(user.getEmail()) != null) {
            // throw new IllegalArgumentException("이미 가입한 회원 이메일 입니다.");
            throw new CustomRuntimeException(ExceptionType.USER_EMAIL_ALREADY_EXISTS, user.getEmail());
        }
        if (userRepository.findByName(user.getName()) != null) {
            // throw new IllegalArgumentException("이미 가입한 회원 이름 입니다.");
            throw new CustomRuntimeException(ExceptionType.USER_USERNAME_ALREADY_EXISTS, user.getName());
        }
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        // 프로필 이미지 데이터 들어왔으면 BinaryContent 생성 및 저장
        if (userCreateRequestDto.getBinaryContentCreateRequestDto() != null) {
            BinaryContent profileImage = userCreateRequestDto.getBinaryContentCreateRequestDto().toEntity();
            binaryContentRepository.save(profileImage);
        }

        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public UserResponseDto readUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            // throw new IllegalArgumentException("존재하지 않는 유저입니다: " + id);
            throw new CustomRuntimeException(ExceptionType.USER_NOT_FOUND, id);
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public List<UserResponseDto> readAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> responses = new ArrayList<>();
        for (User user : users) {
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
            responses.add(UserResponseDto.from(user, userStatus));
        }
        return responses;
    }

    @Override
    //프로필 업데이트 -> 프로필사진이랑 이름
    public UserResponseDto updateUser(UUID id, UserUpdateRequestDto requestDto) {
        User target = userRepository.findById(id);
        if (Objects.isNull(target)) {
            // throw new RuntimeException("해당 유저가 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.USER_NOT_FOUND, id);
        }
        //이름 받으면 업데이트
        if(requestDto.getName() != null) {
            target.setName(requestDto.getName());
        }
        //컨텐츠 받으면 기존꺼 삭제하고 업데이트
        if (requestDto.getProfileImageRequestDto() != null) {
            BinaryContent oldContent = binaryContentRepository.findById(id);
            if (target != null) { //삭제
                binaryContentRepository.delete(oldContent.getId());
            }
            //새로 지정 후 저장
            BinaryContent newContent = requestDto.getProfileImageRequestDto().toEntity();
            binaryContentRepository.save(newContent);
        }
        target.setUpdatedAt();
        userRepository.save(target);

        UserStatus userStatus = userStatusRepository.findByUserId(id);
        return UserResponseDto.from(target, userStatus);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id);
        //cacade: 유저삭제시 연쇄삭제
        UserStatus userStatus = userStatusRepository.findByUserId(id);
        if (userStatus != null) {
            userStatusRepository.delete(userStatus.getId());
        }
        BinaryContent binaryContent = binaryContentRepository.findByUserId(id);
        if (binaryContent != null) {
            binaryContentRepository.delete(binaryContent.getId());
        }
    }
}
