package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.NotFoundUserException;
import com.sprint.mission.discodeit.user.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
                .orElseThrow(NotFoundUserException::new);

        UserStatus userStatus = UserStatus.create(user);
        // User 가 존재하지 않으면 예외
        if(userRepository.findById(userStatus.getUser().getId()).isEmpty()){
            throw new NotFoundUserException();
        }

        // UserStatus가 이미 존재하면 예외
        if (userStatusRepository.findByUserId(userStatus.getUser().getId()).isPresent()){
            throw new DuplicateStatus();
        }

        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusDto find(UUID id){
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusDto> findAll(){
        return userStatusRepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserStatusDto update(UUID id){
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequestDto request){
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        userStatus.updateLastAccessAt();
//        userStatusRepository.update(userStatus);

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id){
        // 있는지 없는지 확인
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userStatusRepository.deleteById(userStatus.getId());
    }


}
