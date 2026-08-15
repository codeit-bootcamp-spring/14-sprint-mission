package com.sprint.mission.discodeit.service.domain.userstatus;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Qualifier("userStatusService")
public class UserStatusDomainServiceImpl implements UserStatusDomainService {

    private final UserStatusRepository userStatusRepository;

    public UserStatusDomainServiceImpl(
            UserStatusRepository userStatusRepository
    ) {
        this.userStatusRepository = userStatusRepository;
    }


    @Override
    public UserStatus create(UserStatus userStatus) {
        // 이미 해당 id를 가진 user의 userStatus 객체가 존재함
        if (userStatusRepository.findByUserId(userStatus.getUserId()).isPresent()) {
            throw new DiscodeitException(
                    ExceptionType.USER_STATUS_ALREADY_EXISTS,
                    userStatus.getUserId()
            );
        }

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        if (Objects.isNull(userStatusId)) {
            throw new DiscodeitException(ExceptionType.USER_STATUS_ID_IS_NULL);
        }

        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() ->
                        new DiscodeitException(
                                ExceptionType.USER_STATUS_NOT_FOUND,
                                userStatusId
                        )
                );
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new DiscodeitException(ExceptionType.USER_ID_IS_NULL);
        }

        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new DiscodeitException(
                                ExceptionType.USER_STATUS_NOT_FOUND,
                                userId
                        )
                );
    }

    @Override
    public List<UserStatus> findAll() { // Optional로
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatus updatingUserStatus) {
        findById(updatingUserStatus.getId());
        return userStatusRepository.save(updatingUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        findById(userStatusId);
        userStatusRepository.delete(userStatusId);
    }
}
