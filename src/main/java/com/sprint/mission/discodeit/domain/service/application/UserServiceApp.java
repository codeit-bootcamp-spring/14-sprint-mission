package com.sprint.mission.discodeit.domain.service.application;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.service.binarycontent.BinaryContentService;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.domain.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.web.controller.dto.req.UserCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.UserLoginRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.UserResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceApp {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO) {
        UUID profileImageId = null;
        if(Objects.nonNull(userCreateRequestDTO.getProfileImage())){
            BinaryContent binaryContent  = binaryContentService.storeFile(userCreateRequestDTO.getProfileImage());
            profileImageId = binaryContent.getId();
        }

        String encodedPassword = passwordEncoder.encode(userCreateRequestDTO.getUserPassword());
        User user = User.init(userCreateRequestDTO.getEmail(),
            encodedPassword, userCreateRequestDTO.getName(),
            userCreateRequestDTO.getAge());

        if(Objects.nonNull(profileImageId)){
            user.updateProfileImage(profileImageId);
        }

        user = userService.createUser(user);

        UserStatus userStatus = UserStatus.init(user.getId());
        userStatusService.createUserStatus(userStatus);

        return UserResponseDTO.of(user, userStatus.isActivated());
    }

    public void deleteUserAccount(UUID id){
        User user = userService.findById(id);
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
            .collect(Collectors.toMap(UserStatus::getUserId, UserStatus::isActivated));

        return userList.stream()
            .map(user -> UserResponseDTO.of(user, uuidBooleanMap.get(user.getId())))
            .toList();
    }

    public User login(UserLoginRequestDTO userLoginRequestDTO){
        User user = userService.findUserByEmail(userLoginRequestDTO.email());
        user.verifyPassword(passwordEncoder, userLoginRequestDTO.password());

        return user;
    }
}
