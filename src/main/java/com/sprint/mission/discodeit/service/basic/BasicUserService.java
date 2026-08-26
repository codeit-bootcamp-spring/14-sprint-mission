package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BasicUserService implements UserService {

    UserRepository userRepository;
    BinaryContentRepository binaryContentRepository;
    UserStatusRepository userStatusRepository;

    @Override
    public UserDto create(
        UserCreateRequest request,
        BinaryContentCreateRequest profileImageRequest) {

        if (userRepository.findByUserName(request.userName()).isPresent()) {
            throw  new DiscodeitException(ErrorCode.DUPLICATE_USERNAME,"이미 사용 중인 userName입니다: " + request.userName());
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_EMAIL,"이미 사용 중인 email입니다: " + request.email());
        }

        UUID profileId = null;
        if (profileImageRequest != null) {
            BinaryContent binaryContent = BinaryContent.builder()
                .fileName(profileImageRequest.fileName())
                .contentType(profileImageRequest.contentType())
                .bytes(profileImageRequest.bytes())
                .size(profileImageRequest.bytes()
                    == null ? 0 : profileImageRequest.bytes().length)
                .build();

            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }

        User user = User.builder()
            .userName(request.userName())
            .email(request.email())
            .password(request.password())
            .nickName(request.nickName())
            .profileId(profileId)
            .build();
            userRepository.save(user);


        UserStatus userStatus = UserStatus.builder()
            .userId(user.getId())
            .lastActiveAt(Instant.now())
            .build();
        userStatusRepository.save(userStatus);

        return toDto(user, userStatus.isOnline());
    }


    @Override
    public Optional<UserDto> findById(UUID id) {
        return userRepository.findById(id)
            .map(user -> toDto(user,isOnline(user.getId())));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(user->toDto(user,isOnline(user.getId())))
            .toList();
    }

    @Override
    public UserDto update(UUID id, UserUpdateRequest request,
        BinaryContentCreateRequest profileImageRequest) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수가 없습니다. 유저ID(" + id + ")"));

        if (request.nickName() != null) {
            user.update(request.nickName());
        }

        if (request.password() != null) {
            user.updatePassword(request.password());
        }

        if (profileImageRequest != null) {
            if (user.getProfileId() != null) {
                binaryContentRepository.delete((user.getProfileId()));
            }

            BinaryContent newBinaryContent = BinaryContent.builder()
                .fileName(profileImageRequest.fileName())
                .contentType(profileImageRequest.contentType())
                .bytes(profileImageRequest.bytes())
                .size(profileImageRequest.bytes() == null ? 0 : profileImageRequest.bytes().length)
                .build();

            binaryContentRepository.save(newBinaryContent);
            user.updateProfileId(newBinaryContent.getId());
        }

        userRepository.update(user);
        return toDto(user, isOnline(user.getId()));
    }



    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND,"해당 유저가 없습니다. 유저ID:" +id+")"));

        if (user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }

        userStatusRepository.findAll().stream()
            .filter(status -> status.getUserId().equals(id))
            .findFirst()
            .ifPresent(status -> userStatusRepository.delete(status.getId()));


        userRepository.delete(id);

    }

    private boolean isOnline(UUID userId) {
        return userStatusRepository.findAll().stream()
            .filter(status -> status.getUserId().equals(userId))
            .findFirst()
            .map(UserStatus::isOnline)
            .orElse(false);


    }

        private static UserDto toDto(User user, boolean online) {
            return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getNickName(),
                user.getProfileId(),
                online,
                user.getCreatedAt()
            );
        }

    }





