package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateUserStatusException;
import com.sprint.mission.discodeit.common.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public UserStatusDto create(UserStatusCreateRequestDto request){

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("사용자 상태 생성 실패 - 존재하지 않는 사용자: userId = {}", request.userId());
                    return new UserNotFoundException(request.userId());
                });

        UserStatus userStatus = UserStatus.create(user);
        // User 가 존재하지 않으면 예외
        if(userRepository.findById(userStatus.getUser().getId()).isEmpty()){
            log.warn("사용자 상태 생성 실패 - 존재하지 않는 사용자: userId = {}", request.userId());
            throw new UserNotFoundException(request.userId());
        }

        // UserStatus가 이미 존재하면 예외
        if (userStatusRepository.findByUserId(userStatus.getUser().getId()).isPresent()){
            log.warn("사용자 상태 생성 실패 - 이미 존재함: userId = {}", request.userId());
            throw new DuplicateUserStatusException(request.userId());
        }

        userStatusRepository.save(userStatus);
        log.info("사용자 상태 생성 성공 - userStatusId = {}, userId = {}", userStatus.getId(), request.userId());
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusDto find(UUID id){
        UserStatus userStatus = check(id);

        log.debug("사용자 상태 조회 성공 - userStatusId = {}", id);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusDto> findAll(){
        List<UserStatusDto> userStatuses = userStatusRepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .toList();
        log.debug("사용자 상태 목록 조회 성공 - count = {}", userStatuses.size());
        return userStatuses;
    }

//    @Override
//    @Transactional
//    public UserStatusDto update(UUID id){
//        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
//        userStatus.updateLastAccessAt();
//
//        return userStatusMapper.toDto(userStatus);
//    }

    @Override
    @Transactional
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequestDto request){
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> {
            log.warn("사용자 상태 수정 실패 - 상태 정보 없음: userId = {}", userId);
            return UserStatusNotFoundException.byUserId(userId);
        });
        userStatus.updateLastAccessAt(request.newLastActiveAt());
//        userStatusRepository.update(userStatus);

        // 접속 중 주기적으로 호출되는 잦은 갱신이라 debug
        log.debug("사용자 상태 수정 성공 - userId = {}", userId);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id){
        // 있는지 없는지 확인
        UserStatus userStatus = check(id);
        userStatusRepository.deleteById(userStatus.getId());
        log.info("사용자 상태 삭제 성공 - userStatusId = {}", id);
    }

    private UserStatus check(UUID id) {
        return userStatusRepository.findById(id).orElseThrow(() -> {
            log.warn("사용자 상태 찾기 실패 - 존재하지 않는 상태: userStatusId = {}", id);
            return UserStatusNotFoundException.byId(id);
        });
    }


}
