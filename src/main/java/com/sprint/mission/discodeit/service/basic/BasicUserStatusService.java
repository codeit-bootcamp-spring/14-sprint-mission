package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateStatus;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
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
        return UserStatusResponseDto.from(userStatus.getUserId(), userStatus.getLastAccessAt());
    }

    @Override
    public UserStatusResponseDto find(UUID id){
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return UserStatusResponseDto.from(userStatus.getUserId(), userStatus.getLastAccessAt());
    }

    @Override
    public List<UserStatusResponseDto> findAll(){
        return userStatusRepository.findAll().stream()
                .map(userStatus ->
                        UserStatusResponseDto.from(userStatus.getUserId(), userStatus.getLastAccessAt()))
                .toList();
    }

    @Override
    public UserStatusResponseDto update(UserStatusUpdateRequestDto request){
        UserStatus userStatus = userStatusRepository.findById(request.id()).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();
        userStatusRepository.update(userStatus);

        return UserStatusResponseDto.from(userStatus.getUserId(), userStatus.getLastAccessAt());
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId){
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();
        userStatusRepository.update(userStatus);

        return UserStatusResponseDto.from(userStatus.getUserId(), userStatus.getLastAccessAt());
    }

    @Override
    public void delete(UserStatusUpdateRequestDto request){
        // 있는지 없는지 확인
        UserStatus userStatus = userStatusRepository.findById(request.id()).orElseThrow(NoSuchElementException::new);
        userStatusRepository.deleteById(userStatus.getId());
    }


}
