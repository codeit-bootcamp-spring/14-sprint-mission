package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentUploadRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createUser(String username, String email, String password,
        BinaryContentUploadRequest profileRequest) {
        User user = User.create(username, email, password);

        if (profileRequest != null) {
            BinaryContent profile = saveProfileImage(profileRequest);
            user.update(null, null, profile);
        }

        UserStatus userStatus = UserStatus.create(user);
        user.updateStatus(userStatus);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDto updateUserInfo(UUID userId, String username, String email,
        BinaryContentUploadRequest profileRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        BinaryContent newProfile = null;
        if (profileRequest != null) {
            newProfile = saveProfileImage(profileRequest);
        }
        user.update(username, email, newProfile);

        return userMapper.toDto(user);
    }

    private BinaryContent saveProfileImage(BinaryContentUploadRequest request) {
        BinaryContent profile = BinaryContent.create(
            request.fileName(),
            request.size(),
            request.contentType()
        );
        BinaryContent savedProfile = binaryContentRepository.save(profile);
        binaryContentStorage.put(savedProfile.getId(), request.bytes());
        return savedProfile;
    }

    @Transactional
    public void changePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.updatePassword(newPassword);
    }

    public String getUserProfileFileName(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (user.getProfile() == null) {
            return "기본프로필.png";
        }

        return user.getProfile().getFileName();
    }


    @Transactional
    public void updateUserActivity(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (user.getStatus() != null) {
            user.getStatus().updateLastActiveAt();
        }
    }
}