//package com.sprint.mission.application.userstatus;
//
//import com.sprint.mission.domain.UserStatus;
//import com.sprint.mission.dto.userstatus.UserStatusCreateRequestDto;
//import com.sprint.mission.dto.userstatus.UserStatusResponseDto;
//import com.sprint.mission.service.user.UserDomainService;
//import com.sprint.mission.service.userstatus.UserStatusDomainService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserStatusApplicationServiceImpl implements UserStatusApplicationService {
//
//    private final UserStatusDomainService userStatusDomainService;
//    private final UserDomainService userDomainService;
//
//
//    @Override
//    public UserStatusResponseDto create(
//            UserStatusCreateRequestDto userStatusCreateRequest
//    ) {
//        // User가 존재하지 않거나 이미 해당 user로 user status 객체 있으면 예외 발생
//        userDomainService.findById(userStatusCreateRequest.getUserId());
//
//        UserStatus userStatus = UserStatus.create(userStatusCreateRequest.getUserId());
//        UserStatus createdUserStatus = userStatusDomainService.create(userStatus);
//
//        log.info(
//                "UserStatus 생성 완료: userStatusId={}, userId={}",
//                createdUserStatus.getId(),
//                createdUserStatus.getUserId()
//        );
//        return UserStatusResponseDto.from(createdUserStatus);
//    }
//
//    @Override
//    public UserStatusResponseDto findById(UUID userStatusId) {
//        log.debug(
//                "UserStatus 단건 조회: userStatusId={}",
//                userStatusId
//        );
//        UserStatus userStatus = userStatusDomainService.findById(userStatusId);
//        return UserStatusResponseDto.from(userStatus);
//    }
//
//    @Override
//    public List<UserStatusResponseDto> findAll() {
//        List<UserStatusResponseDto> responses =
//                userStatusDomainService.findAll()
//                        .stream()
//                        .map(UserStatusResponseDto::from)
//                        .toList();
//
//        log.debug(
//                "UserStatus 목록 조회 완료: count={}",
//                responses.size()
//        );
//
//        return responses;
//    }
//
//    // UserStatus id로 lastActiveAt 시간 업데이트
//    @Override
//    public UserStatusResponseDto update(UUID userStatusId) {
//        UserStatus updatingUserStatus = userStatusDomainService.findById(userStatusId);
//        updatingUserStatus.refreshLastActiveAt();
//        UserStatus updatedUserStatus = userStatusDomainService.update(updatingUserStatus);
//
//        log.debug(
//                "UserStatus 갱신 완료: userStatusId={}, userId={}, lastActiveAt={}",
//                updatedUserStatus.getId(),
//                updatedUserStatus.getUserId(),
//                updatedUserStatus.getLastActiveAt()
//        );
//
//        return UserStatusResponseDto.from(updatedUserStatus);
//    }
//
//    // User id로 해당 user status의 lastActiveAt 시간 업데이트
//    @Override
//    public UserStatusResponseDto updateByUserId(UUID userId) {
//        UserStatus updatingUserStatus = userStatusDomainService.findByUserId(userId);
//        updatingUserStatus.refreshLastActiveAt();
//        UserStatus updatedUserStatus = userStatusDomainService.update(updatingUserStatus);
//
//        log.debug(
//                "UserStatus 갱신 완료: userId={}, userStatusId={}, lastActiveAt={}",
//                userId,
//                updatedUserStatus.getId(),
//                updatedUserStatus.getLastActiveAt()
//        );
//        return UserStatusResponseDto.from(updatedUserStatus);
//    }
//
//    @Override
//    public void delete(UUID userStatusId) {
//        log.info("UserStatus 삭제 시작: userStatusId={}", userStatusId);
//
//        userStatusDomainService.delete(userStatusId);
//
//        log.info("UserStatus 삭제 완료: userStatusId={}", userStatusId);
//    }
//}
