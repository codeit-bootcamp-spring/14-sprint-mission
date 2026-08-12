package com.sprint.mission.discodeit.service.application.user;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import com.sprint.mission.discodeit.service.domain.userstatus.UserStatusDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserDomainService userDomainService;
    private final BinaryContentDomainService binaryContentDomainService;
    private final UserStatusDomainService userStatusDomainService;
    private final ChannelDomainService channelDomainService;
    private final ReadStatusDomainService readStatusDomainService;

    public UserApplicationServiceImpl(
            UserDomainService userDomainService,
            BinaryContentDomainService binaryContentDomainService,
            UserStatusDomainService userStatusDomainService,
            ChannelDomainService channelDomainService,
            ReadStatusDomainService readStatusDomainService
    ) {
        this.userDomainService = userDomainService;
        this.binaryContentDomainService = binaryContentDomainService;
        this.userStatusDomainService = userStatusDomainService;
        this.channelDomainService = channelDomainService;
        this.readStatusDomainService = readStatusDomainService;
    }

    @Override
    public UserResponseDto create(
            UserCreateRequestDto userCreateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    ) {
        BinaryContent createdProfile = null;
        User createdUser = null;
        UserStatus createdUserStatus = null;

        try {
            createdProfile = createProfileImage(profileImageRequest);
            UUID createdProfileId = Objects.nonNull(createdProfile)
                    ? createdProfile.getId()
                    : null;

            log.info(
                    "User 생성 시작: username={}, email={}, profileImage={}",
                    userCreateRequest.getUsername(),
                    userCreateRequest.getEmail(),
                    Objects.isNull(createdProfileId) ? "N/A" : createdProfileId
            );

            createdUser = userDomainService.create(User.create(
                    userCreateRequest.getUsername(),
                    userCreateRequest.getEmail(),
                    userCreateRequest.getPassword(),
                    createdProfileId
            ));

            createdUserStatus = userStatusDomainService.create(
                    UserStatus.create(createdUser.getId())
            );
            createPublicChannelReadStatuses(createdUser.getId());

            log.info(
                    "User 생성 완료: userId={}, profileId={}",
                    createdUser.getId(),
                    createdUser.getProfileId()
            );

            return UserResponseDto.from(createdUser, createdUserStatus);
        } catch (RuntimeException originalException) {
            rollbackUserCreation(
                    createdProfile,
                    createdUser,
                    createdUserStatus,
                    originalException
            );
            throw originalException;
        }
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
        List<UserResponseDto> userResponses =
                toDto(users, userStatuses);

        log.debug("User 다건 조회: size={}", userResponses.size());

        return userResponses;
    }

    @Override
    public UserResponseDto update(
            UUID userId,
            UserUpdateRequestDto userUpdateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    ) {
        User user = userDomainService.findById(userId);
        User originalUserState = User.create(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileId()
        );
        UUID oldProfileId = user.getProfileId();
        BinaryContent oldProfileContent = findProfileImage(oldProfileId);
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        UUID newProfileId = oldProfileId;
        BinaryContent newProfileContent = null;
        boolean newProfileCreated = false;
        boolean userUpdated = false;
        boolean oldProfileDeleted = false;

        log.info(
                "User 수정 시작: userId={}, replaceProfile={}",
                userId,
                Objects.nonNull(profileImageRequest) ? "YES" : "N/A"
        );

        try {
            if (Objects.nonNull(profileImageRequest)) {
                newProfileContent = createProfileImage(profileImageRequest);
                newProfileId = newProfileContent.getId();
                newProfileCreated = true;

                log.debug(
                        "새 프로필 저장 완료: userId={}, newProfileId={}",
                        userId,
                        newProfileId
                );
            }

            User userUpdates = User.create(
                    userUpdateRequest.getUsername(),
                    userUpdateRequest.getEmail(),
                    userUpdateRequest.getPassword(),
                    newProfileId
            );

            User updatedUser = userDomainService.update(userId, userUpdates);
            userUpdated = true;

            if (Objects.nonNull(newProfileContent) && Objects.nonNull(oldProfileId)) {
                binaryContentDomainService.delete(oldProfileId);
                oldProfileDeleted = true;
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
        } catch (RuntimeException originalException) {
            rollbackUserUpdate(
                    userId,
                    originalUserState,
                    oldProfileContent,
                    oldProfileDeleted,
                    userUpdated,
                    newProfileContent,
                    newProfileCreated,
                    originalException
            );
            throw originalException;
        }
    }

    @Override
    public void delete(UUID userId) {
        User user = userDomainService.findById(userId);
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        UUID profileId = user.getProfileId();
        BinaryContent profile = findProfileImage(profileId);

        log.info("User 삭제 시작: userId={}", userId);

        boolean userStatusDeleted = false;
        boolean userDeleted = false;
        boolean profileDeleted = false;

        try {
            userStatusDomainService.delete(userStatus.getId());
            userStatusDeleted = true;

            userDomainService.delete(userId);
            userDeleted = true;

            if (Objects.nonNull(profile)) {
                binaryContentDomainService.delete(profile.getId());
                profileDeleted = true;
            }

            log.info(
                    "User 삭제 완료: userId={}, userStatusId={}, profileId={}",
                    userId,
                    userStatus.getId(),
                    profileId
            );
        } catch (RuntimeException originalException) {
            rollbackUserDeletion(
                    user,
                    userStatus,
                    profile,
                    userStatusDeleted,
                    userDeleted,
                    profileDeleted,
                    originalException
            );
            throw originalException;
        }
    }

    private BinaryContent createProfileImage(
            BinaryContentCreateRequestDto profileImageRequest
    ) {
        if (Objects.isNull(profileImageRequest)) {
            return null;
        }

        BinaryContent profile = BinaryContent.create(
                profileImageRequest.getFileName(),
                profileImageRequest.getBytes()
        );
        return binaryContentDomainService.create(profile);
    }

    private BinaryContent findProfileImage(UUID profileId) {
        return Objects.nonNull(profileId)
                ? binaryContentDomainService.findById(profileId)
                : null;
    }

    private void createPublicChannelReadStatuses(UUID userId) {
        List<ReadStatus> readStatuses = channelDomainService.findAll()
                .stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC)
                .map(channel -> ReadStatus.create(userId, channel.getId()))
                .toList();

        readStatusDomainService.createAll(readStatuses);
    }

    private List<UserResponseDto> toDto(
            List<User> users,
            List<UserStatus> userStatuses
    ) {
        Map<UUID, UserStatus> userStatusMap = new HashMap<>();
        for (UserStatus userStatus : userStatuses) {
            userStatusMap.put(userStatus.getUserId(), userStatus);
        }

        List<UserResponseDto> userResponses = new ArrayList<>();
        for (User user : users) {
            UserStatus userStatus = userStatusMap.get(user.getId());
            if (Objects.isNull(userStatus)) {
                throw new CustomException(
                        ExceptionType.USER_STATUS_NOT_FOUND,
                        user.getId()
                );
            }

            userResponses.add(UserResponseDto.from(user, userStatus));
        }

        return userResponses;
    }

    private void rollbackUserCreation(
            BinaryContent createdProfile,
            User createdUser,
            UserStatus createdUserStatus,
            RuntimeException originalException
    ) {
        if (Objects.nonNull(createdUserStatus)) {
            try {
                userStatusDomainService.delete(createdUserStatus.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (Objects.nonNull(createdUser)) {
            try {
                userDomainService.delete(createdUser.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (Objects.nonNull(createdProfile)) {
            try {
                binaryContentDomainService.delete(createdProfile.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }

    private void rollbackUserUpdate(
            UUID userId,
            User originalUserState,
            BinaryContent oldProfileContent,
            boolean oldProfileDeleted,
            boolean userUpdated,
            BinaryContent newProfileContent,
            boolean newProfileCreated,
            RuntimeException originalException
    ) {
        if (oldProfileDeleted) {
            try {
                binaryContentDomainService.create(oldProfileContent);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (userUpdated) {
            try {
                userDomainService.update(userId, originalUserState);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (newProfileCreated) {
            try {
                binaryContentDomainService.delete(newProfileContent.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }

    private void rollbackUserDeletion(
            User user,
            UserStatus userStatus,
            BinaryContent profile,
            boolean userStatusDeleted,
            boolean userDeleted,
            boolean profileDeleted,
            RuntimeException originalException
    ) {
        if (profileDeleted) {
            try {
                binaryContentDomainService.create(profile);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (userDeleted) {
            try {
                userDomainService.create(user);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (userStatusDeleted) {
            try {
                userStatusDomainService.create(userStatus);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
