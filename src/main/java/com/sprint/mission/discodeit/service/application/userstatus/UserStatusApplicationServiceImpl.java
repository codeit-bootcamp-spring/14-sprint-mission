package com.sprint.mission.discodeit.service.application.userstatus;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import com.sprint.mission.discodeit.service.domain.userstatus.UserStatusDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserStatusApplicationServiceImpl implements UserStatusApplicationService {

    private final UserStatusDomainService userStatusDomainService;
    private final UserDomainService userDomainService;

    public UserStatusApplicationServiceImpl(
            UserStatusDomainService userStatusDomainService,
            UserDomainService userDomainService
    ) {
        this.userStatusDomainService = userStatusDomainService;
        this.userDomainService = userDomainService;
    }

    @Override
    public UserStatusResponseDto create(
            UserStatusCreateRequestDto userStatusCreateRequest
    ) {
        // User가 존재하지 않거나 이미 해당 user로 user status 객체 있으면 예외 발생
        userDomainService.findById(userStatusCreateRequest.getUserId());

        UserStatus userStatus = UserStatus.create(userStatusCreateRequest.getUserId());
        UserStatus createdUserStatus = userStatusDomainService.create(userStatus);

        log.info(
                "UserStatus 생성 완료: userStatusId={}, userId={}",
                createdUserStatus.getId(),
                createdUserStatus.getUserId()
        );
        return UserStatusResponseDto.from(createdUserStatus);
    }

    @Override
    public UserStatusResponseDto findById(UUID userStatusId) {
        log.debug(
                "UserStatus 단건 조회: userStatusId={}",
                userStatusId
        );
        UserStatus userStatus = userStatusDomainService.findById(userStatusId);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        List<UserStatusResponseDto> responses =
                userStatusDomainService.findAll()
                        .stream()
                        .map(UserStatusResponseDto::from)
                        .toList();

        log.debug(
                "UserStatus 목록 조회 완료: count={}",
                responses.size()
        );

        return responses;
    }

    // UserStatus id로 lastActiveAt 시간 업데이트
    @Override
    public UserStatusResponseDto update(UUID userStatusId) {
        UserStatus userStatus = userStatusDomainService.update(userStatusId);
        log.debug(
                "UserStatus 갱신 완료: userStatusId={}, userId={}, lastActiveAt={}",
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt()
        );
        return UserStatusResponseDto.from(userStatus);
    }

    // User id로 해당 user status의 lastActiveAt 시간 업데이트
    @Override
    public UserStatusResponseDto updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusDomainService.updateByUserId(userId);
        log.debug(
                "UserStatus 갱신 완료: userId={}, userStatusId={}, lastActiveAt={}",
                userId,
                userStatus.getId(),
                userStatus.getLastActiveAt()
        );
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        log.info("UserStatus 삭제 시작: userStatusId={}", userStatusId);

        userStatusDomainService.delete(userStatusId);

        log.info("UserStatus 삭제 완료: userStatusId={}", userStatusId);
    }
}
