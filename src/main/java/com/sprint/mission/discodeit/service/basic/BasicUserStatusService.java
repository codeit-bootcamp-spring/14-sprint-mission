package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BasicUserStatusService implements UserStatusService {

    // 순환 참조 방지를 위해 repository만 주입.
    UserStatusRepository userStatusRepository;
    UserRepository userRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        if (!userStatusRepository.existsByUserId(request.userId())){
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }
        if (userStatusRepository.existsByUserId(request.userId())){
            throw new NoSuchElementException("해당 유저의 상태 정보가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(request.userId());
        userStatusRepository.save(userStatus);
        return toResponse(userStatus);
    }

    @Override
    public UserStatusResponse find(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다."));
        return toResponse(userStatus);
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    @Override
    public UserStatusResponse update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다."));

        userStatus.update();
        userStatusRepository.save(userStatus);

        return toResponse(userStatus);
    }

    @Override
    public UserStatusResponse updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다."));

        userStatus.update();
        userStatusRepository.save(userStatus);

        return toResponse(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsByUserId(id)){
            throw new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다.");
        }
        userStatusRepository.deleteByUserId(id);
    }

    private UserStatusResponse toResponse(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.isOnline()
        );
    }
}
