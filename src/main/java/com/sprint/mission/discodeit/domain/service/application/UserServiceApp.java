package com.sprint.mission.discodeit.domain.service.application;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.service.binarycontent.BinaryContentService;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.domain.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.web.controller.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserLoginRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserResponseDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserUpdateResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceApp {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO, MultipartFile profileImage) {
        UUID profileImageId = null;
        if(Objects.nonNull(profileImage) && !profileImage.isEmpty()){
            profileImageId = storeProfileImage(profileImage);
        }

        String encodedPassword = passwordEncoder.encode(userCreateRequestDTO.getPassword());
        User user = User.init(userCreateRequestDTO.getEmail(),
            encodedPassword, userCreateRequestDTO.getUsername());

        if(Objects.nonNull(profileImageId)){
            user.updateProfileImage(profileImageId);
        }

        user = userService.createUser(user);

        UserStatus userStatus = UserStatus.init(user.getId());
        userStatusService.createUserStatus(userStatus);

        return UserResponseDTO.of(user, userStatus.isActive());
    }

    public void deleteUserAccount(UUID id){
        User user = userService.findUserById(id);
        if(user.hasProfileImage()){
            binaryContentService.deleteStoreFileById(user.getProfileId());
        }
        userStatusService.deleteUserStatusByUserId(user.getId());
        userService.deleteUser(user.getId());
    }

    /*
        코드잇 미션용
     */
    public List<UserResponseDTO> findAllUser(){
        List<User> userList = userService.findAllUser();
        List<UserStatus> userStatusList = userStatusService.findAllUserStatus();

        Map<UUID, Boolean> uuidBooleanMap = userStatusList.stream()
            .collect(Collectors.toMap(UserStatus::getUserId, UserStatus::isActive));

        return userList.stream()
            .map(user -> UserResponseDTO.of(user, uuidBooleanMap.get(user.getId())))
            .toList();
    }

    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO){
        // 이건또 왜 이름으로 받지
        log.info("{}", userLoginRequestDTO  );
        User user = userService.findUserByEmail(userLoginRequestDTO.username());
        log.info("{}", user);
        user.verifyPassword(passwordEncoder, userLoginRequestDTO.password());
        log.info("sadsadsdasdaasd");
        UserStatus userStatus = userStatusService.findUserStatusByUserId(user.getId());

        userStatus.login();
        //todo : 다시
        userStatusService.updateUserStatusByUserId(user.getId());

        return UserResponseDTO.of(user, true);
    }


    // 8.23 추가 - 기존 업데이트 (이름만 ) -> 요구사항대로 모든필드/이미지 다 받는거로
    //todo : 파일 따라서 다르게
    public UserUpdateResponseDTO updateUser(UUID userId, UserUpdateRequestDTO userUpdateRequestDTO, MultipartFile profileImage){
        UUID profileImageId = null;
        if(Objects.nonNull(profileImage) && !profileImage.isEmpty()){
            profileImageId = storeProfileImage(profileImage);
        }

        User updatedUser = userService.updateUser(userId, userUpdateRequestDTO.newUserName(),
            userUpdateRequestDTO.newEmail(), userUpdateRequestDTO.newPassword(), profileImageId);

        return UserUpdateResponseDTO.from(updatedUser);
    }

    private UUID storeProfileImage(MultipartFile profileImage){
        BinaryContent binaryContent = binaryContentService.storeFile(profileImage);
        return binaryContent.getId();
    }
}
