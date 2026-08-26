package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.NotFoundUserException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto request){

        UserStatus userStatus = request.toEntity();
        // User 가 존재하지 않으면 예외
        if(userRepository.findById(userStatus.getUserId()).isEmpty()){
            throw new NotFoundUserException();
        }

        // UserStatus가 이미 존재하면 예외
        if (userStatusRepository.findByUserId(userStatus.getUserId()).isPresent()){
            throw new DuplicateStatus();
        }

        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                                          userStatus.getUserId(), userStatus.getLastAccessAt(), userStatus.isOnline());
    }

    @Override
    public UserStatusResponseDto find(UUID id){
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return UserStatusResponseDto.from(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                                          userStatus.getUserId(), userStatus.getLastAccessAt(), userStatus.isOnline());
    }

    @Override
    public List<UserStatusResponseDto> findAll(){
        return userStatusRepository.findAll().stream()
                .map(userStatus ->
                        UserStatusResponseDto.from(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                                userStatus.getUserId(), userStatus.getLastAccessAt(), userStatus.isOnline()))
                .toList();
    }

    @Override
    public UserStatusResponseDto update(UUID id){
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();
        userStatusRepository.update(userStatus);

        return UserStatusResponseDto.from(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                                          userStatus.getUserId(), userStatus.getLastAccessAt(), userStatus.isOnline());
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateRequestDto request){
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();
        userStatusRepository.update(userStatus);

        return UserStatusResponseDto.from(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                                          userStatus.getUserId(), userStatus.getLastAccessAt(), userStatus.isOnline());
    }

    @Override
    public void delete(UUID id){
        // 있는지 없는지 확인
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userStatusRepository.deleteById(userStatus.getId());
    }


}
