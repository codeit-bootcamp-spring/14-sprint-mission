package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.binarycontent.application.BinaryApplicationService;
import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.entity.UserStatus;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.user.domain.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.domain.service.UserService;
import com.sprint.mission.discodeit.user.web.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.req.UserLoginRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.req.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserStatusResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserApplicationService {
    private final BinaryApplicationService binaryApplicationService;
    private final UserService userService;

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    public UserResponseDTO createAccount(UserCreateRequestDTO request, MultipartFile profileImage) {
        userService.validateUsernameNotDuplicated(request.username());

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.init(
            request.email(), encodedPassword, request.username(), null
        );

        if(Objects.nonNull(profileImage) && !profileImage.isEmpty()){
            user.updateProfileImage(storeProfileImage(profileImage));
        }

        user = userRepository.save(user);
        UserStatus userStatus = UserStatus.init(user);

        userStatusRepository.save(userStatus);

        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUserAccount(UUID userId){
        User user = userRepository.getByIdOrThrow(userId);
//        if(user.hasProfileImage()){
//            binaryContentService.deleteStoreFileById(user.getProfileId());
//        }

        userRepository.delete(user);
    }

    @Transactional
    public List<UserResponseDTO> findAllUser(){
        List<User> userList = userRepository.findAllWithProfileImageAndUserStatus();
//        List<UserStatus> userStatusList = userStatusRepository.findAll();
//
//        Map<UUID, Boolean> uuidBooleanMap = userStatusList.stream()
//            .collect(Collectors.toMap(
//                userStatus -> userStatus.getUser().getId(),
//                UserStatus::isActive
//            ));

        return userList.stream()
            .map(userMapper::toResponse)
            .toList();
    }

    @Transactional
    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO){
        // 이름으로 받음 - 이메일아님
        User user = userRepository.getByNameOrThrow(userLoginRequestDTO.username());
        user.verifyPassword(passwordEncoder, userLoginRequestDTO.password());

        UserStatus userStatus = user.getUserStatus();
        userStatus.login();

        return userMapper.toResponse(user);
    }


    @Transactional
    public UserResponseDTO updateUser(UUID userId, UserUpdateRequestDTO request, MultipartFile profileImage){
        User user = userRepository.getByIdOrThrow(userId);

        if (!user.getName().equals(request.newUsername())) {
            userService.validateUsernameNotDuplicated(request.newUsername());
        }

        if (!user.getEmail().equals(request.newEmail())) {
            userService.validateEmailNotDuplicated(request.newEmail());     // 하나로 할지 ㅁㅁ
        }

        user.updateAllField(
            request.newUsername(),
            request.newEmail(),
            passwordEncoder.encode(request.newPassword())
        );

        if(Objects.nonNull(profileImage) && !profileImage.isEmpty()){
            BinaryContent binaryContent = storeProfileImage(profileImage);
            user.updateProfileImage(binaryContent);
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserStatusResponseDTO updateUserStatus(UUID userId, Instant activeAt) {
        UserStatus userStatus = userStatusRepository.getByUserIdOrThrow(userId);

        userStatus.activateUser(activeAt);
        return userStatusMapper.toResponse(userStatus);
    }

    private BinaryContent storeProfileImage(MultipartFile profileImage){
        return binaryApplicationService.storeMultipartFile(profileImage);
    }
}
