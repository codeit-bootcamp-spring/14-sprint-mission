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
            // 1. 요청 Dto 받는다
            UserCreateRequestDto userCreateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    ) {

        UUID createdProfileId = null;
        User createdUser = null;
        UserStatus createdUserStatus = null;

        try {
            // 2. User를 생성하는 일련의 과정들의 순서를 결정한다

            // 2-1. 선택적으로 프로필 이미지 등록
            if (Objects.nonNull(profileImageRequest)) {
                createdProfileId = binaryContentDomainService
                        .create(
                                BinaryContent.create(
                                        profileImageRequest.getFileName(),
                                        profileImageRequest.getBytes()
                                )
                        )
                        .getId();
            }

            log.info(
                    "User 생성 시작: username={}, email={}, profileImage={}",
                    userCreateRequest.getUsername(),
                    userCreateRequest.getEmail(),
                    Objects.isNull(createdProfileId) ? "N/A" : createdProfileId
            );

            // 2-2. 보낼 User 생성 (Draft)
            User user = User.create(
                    userCreateRequest.getUsername(),
                    userCreateRequest.getEmail(),
                    userCreateRequest.getPassword(),
                    createdProfileId
            );
            createdUser = userDomainService.create(user);

            // 2-3. User의 User Status 생성
            UserStatus userStatus = UserStatus.create(createdUser.getId());
            createdUserStatus = userStatusDomainService.create(userStatus);

            // 2-4. Public channel에 대한 read status 추가
            User savedUser = createdUser;   // 람다용
            List<ReadStatus> publicChannelReadStatuses =
                    channelDomainService.findAll()
                            .stream()
                            .filter(channel ->
                                    channel.getChannelType() == ChannelType.PUBLIC)
                            .map(channel -> ReadStatus.create(
                                    savedUser.getId(),
                                    channel.getId()
                            ))
                            .toList();

            readStatusDomainService.createAll(publicChannelReadStatuses);

            log.info(
                    "User 생성 완료: userId={}, profileId={}",
                    createdUser.getId(),
                    createdUser.getProfileId()
            );

            // 4. 응답 Dto 조립
            return UserResponseDto.from(createdUser, createdUserStatus);
        } catch (RuntimeException originalException) {
            // 3. 실패시 보상 작업: User 생성 과정에서 어떤 문제가 있었을때 예외 발행. (전부 rollback)

            if (Objects.nonNull(createdUserStatus)) {   // userStatus 만든 게 있으면 삭제
                try {
                    userStatusDomainService.delete(createdUserStatus.getId());
                } catch (RuntimeException rollbackException) {
                    // .addSuppressed
                    // = 메인 예외가 발생해 밖으로 던져질 때,
                    //   그 과정에서 함께 발생했으나 묻힐 뻔한(억제된) 다른 예외들을 주 예외에 첨부해 기록하는 메서드
                    originalException.addSuppressed(rollbackException);
                }
            }

            if (Objects.nonNull(createdUser)) {     // user 만든게 있으면 삭제
                try {
                    userDomainService.delete(createdUser.getId());
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            if (Objects.nonNull(createdProfileId)) { // binary content 만든게 있으면 삭제
                try {
                    binaryContentDomainService.delete(createdProfileId);
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

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

        // userId의 user status를 매핑해서 저장
        // { userId : UserStatus }
        Map<UUID, UserStatus> userStatusMap = new HashMap<>();
        for (UserStatus userStatus : userStatuses) {
            // userStatus에서 userId를 get
            UUID userId = userStatus.getUserId();
            userStatusMap.put(userId, userStatus);
        }

        // DTO(User + User Status)로 user와 user status를 합쳐 dto로 반환한다
        List<UserResponseDto> userResponses = new ArrayList<>();
        for (User user : users) {
            UUID userId = user.getId();
            UserStatus userStatus = userStatusMap.get(userId);

            if (Objects.isNull(userStatus)) {
                throw new CustomException(ExceptionType.USER_STATUS_NOT_FOUND, userId);
            }

            userResponses.add(UserResponseDto.from(user, userStatus));
        }

        log.debug("User 다건 조회: size={}", userResponses.size());

        return userResponses;
    }


    @Override
    public UserResponseDto update(
            UUID userId,
            UserUpdateRequestDto userUpdateRequest,
            BinaryContentCreateRequestDto profileImageRequest
        ) {
        // 실패 가능성이 있는 조회는 상태를 변경하기 전에 모두 끝낸다.
        User user = userDomainService.findById(userId);
        String oldUsername = user.getUsername();
        String oldEmail = user.getEmail();
        String oldPassword = user.getPassword();
        UUID oldProfileId = user.getProfileId();
        BinaryContent oldProfileContent = Objects.nonNull(oldProfileId)
                ? binaryContentDomainService.findById(oldProfileId)
                : null;
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
            // 새 프로필 요청이 있으면 먼저 저장한다.
            if (Objects.nonNull(profileImageRequest)) {
                newProfileContent = BinaryContent.create(
                        profileImageRequest.getFileName(),
                        profileImageRequest.getBytes()
                );
                newProfileId = binaryContentDomainService
                        .create(newProfileContent)
                        .getId();
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

            // 새 프로필로 교체한 경우에만 기존 프로필을 마지막에 삭제한다.
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
            // 기존 프로필 삭제 이후 후속 작업이 실패했다면 기존 프로필부터 복원한다.
            if (oldProfileDeleted) {
                try {
                    binaryContentDomainService.create(oldProfileContent);
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            // User 수정까지 성공한 뒤 기존 프로필 삭제가 실패했다면
            // User가 다시 기존 프로필을 참조하도록 원래 정보로 되돌린다.
            if (userUpdated) {
                try {
                    User originalUserState = User.create(
                            oldUsername,
                            oldEmail,
                            oldPassword,
                            oldProfileId
                    );
                    userDomainService.update(userId, originalUserState);
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            // 새 프로필 저장 이후 어느 단계에서든 실패하면 새 프로필을 제거한다.
            if (newProfileCreated) {
                try {
                    binaryContentDomainService.delete(newProfileContent.getId());
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            throw originalException;
        }
    }


    @Override
    public void delete(UUID userId) {
        User user = userDomainService.findById(userId);
        UserStatus userStatus = userStatusDomainService.findByUserId(userId);

        UUID profileId = user.getProfileId();
        BinaryContent profile = Objects.nonNull(profileId)
                ? binaryContentDomainService.findById(profileId)
                : null;

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
            // user 삭제 과정에서 어떤 문제가 발생해서 exception 발생했을때, 안 삭제해도 됐던 것들을 다시 생성한다
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

            throw originalException;
        }
    }
}
