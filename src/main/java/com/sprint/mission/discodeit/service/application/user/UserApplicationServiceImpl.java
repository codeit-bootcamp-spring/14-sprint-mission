package com.sprint.mission.discodeit.service.application.user;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import com.sprint.mission.discodeit.service.domain.userstatus.UserStatusDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserDomainService userDomainService;
    private final BinaryContentDomainService binaryContentDomainService;
    private final UserStatusDomainService userStatusDomainService;
    private final ChannelDomainService channelDomainService;
    private final ReadStatusDomainService readStatusDomainService;

    @Override
    public UserResponseDto create(
            UserCreateRequestDto userCreateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    ) {
        // generate binary content
        BinaryContent createdProfileImage = (Objects.nonNull(profileImageRequest))
                ? binaryContentDomainService.create(
                    BinaryContent.create(
                            profileImageRequest.getFileName(),
                            profileImageRequest.getBytes()
                    )
                  )
                : null;

        log.info(
                "User 생성 시작: username={}, email={}, profileImage={}",
                userCreateRequest.getUsername(),
                userCreateRequest.getEmail(),
                Objects.isNull(createdProfileImage) ? "No Pfp" : createdProfileImage.getId()
        );

        // create user
        User createdUser = userDomainService.create(User.create(
                userCreateRequest.getUsername(),
                userCreateRequest.getEmail(),
                userCreateRequest.getPassword(),
                createdProfileImage.getId()
        ));

        // create user status to track last read time
        UserStatus createdUserStatus = userStatusDomainService.create(
                UserStatus.create(createdUser.getId())
        );

        // create read status for all public channels
        List<ReadStatus> readStatuses = channelDomainService.findAll()
                .stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC)
                .map(channel -> ReadStatus.create(createdUser.getId(), channel.getId()))
                .toList();
        readStatusDomainService.createAll(readStatuses);

        log.info(
                "User 생성 완료: userId={}, profileId={}",
                createdUser.getId(),
                createdUser.getProfileId()
        );

        return UserResponseDto.from(createdUser, createdUserStatus);
    }

    @Override
    public UserResponseDto findById(UUID userId) {
        log.debug("User 단일 조회: userId={}", userId);

        User user = userDomainService.findById(userId);
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userDomainService.findAll();
        List<UserStatus> userStatuses = userStatusDomainService.findAll();
        List<UserResponseDto> userResponses = toUserResponseDtoList(users, userStatuses);

        log.debug("User 다건 조회: size={}", userResponses.size());

        return userResponses;
    }

    @Override
    public UserResponseDto update(
            UUID userId,
            UserUpdateRequestDto userUpdateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    ) {
        User updatingUser = userDomainService.findById(userId);
        UUID oldProfileId = updatingUser.getProfileId();
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        log.info(
                "User 수정 시작: userId={}, replaceProfile={}",
                userId,
                Objects.nonNull(profileImageRequest) ? "YES" : "N/A"
        );

        BinaryContent createdProfileImage = (Objects.nonNull(profileImageRequest))
                ? binaryContentDomainService.create(
                        BinaryContent.create(
                                profileImageRequest.getFileName(),
                                profileImageRequest.getBytes()
                        )
                )
                : null;

        if (Objects.nonNull(createdProfileImage)) {
            log.debug(
                    "새 프로필 저장 완료: userId={}, newProfileId={}",
                    userId,
                    createdProfileImage
            );
        }

        updatingUser.updateAccountDetails(
                userUpdateRequest.getUsername(),
                userUpdateRequest.getEmail(),
                userUpdateRequest.getPassword(),
                (Objects.isNull(createdProfileImage)
                        ? oldProfileId
                        : createdProfileImage.getId()
                )
        );
        User updatedUser = userDomainService.update(updatingUser);

        if (Objects.nonNull(createdProfileImage) && Objects.nonNull(oldProfileId)) {
            binaryContentDomainService.delete(oldProfileId);
            log.debug(
                    "기존 프로필 삭제 완료: userId={}, oldProfileId={}",
                    userId,
                    oldProfileId
            );
        }

        log.info(
                "User 수정 완료: userId={}, profileId={}",
                updatedUser.getId(),
                updatedUser.getProfileId()
        );

        return UserResponseDto.from(updatedUser, userStatus);
    }

    @Override
    public void delete(UUID userId) {
        User user = userDomainService.findById(userId);
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        UUID profileId = user.getProfileId();
        BinaryContent profileImage = (Objects.nonNull(profileId))
                ? binaryContentDomainService.findById(profileId)
                : null;

        log.info("User 삭제 시작: userId={}", userId);

        userStatusDomainService.delete(userStatus.getId());
        userDomainService.delete(userId);

        if (Objects.nonNull(profileImage)) {
            binaryContentDomainService.delete(profileImage.getId());
        }

        log.info(
                "User 삭제 완료: userId={}, userStatusId={}, profileId={}",
                userId,
                userStatus.getId(),
                profileId
        );
    }


    private List<UserResponseDto> toUserResponseDtoList(
            List<User> users,
            List<UserStatus> userStatuses
    ) {
        // { userId : UserStatus } map
        Map<UUID, UserStatus> userStatusMap = new HashMap<>();
        for (UserStatus userStatus : userStatuses) {
            userStatusMap.put(userStatus.getUserId(), userStatus);
        }

        // construct UserResponseDto
        List<UserResponseDto> userResponses = new ArrayList<>();
        for (User user : users) {
            UserStatus userStatus = userStatusMap.get(user.getId());
            if (Objects.isNull(userStatus)) {
                // 방어적 fail
                log.warn(
                        "UserStatus 누락됨: userId={}, isOnline=false 기본값으로 반환함",
                        user.getId()
                );
            }

            userResponses.add(UserResponseDto.from(user, userStatus));
        }

        return userResponses;
    }

}
