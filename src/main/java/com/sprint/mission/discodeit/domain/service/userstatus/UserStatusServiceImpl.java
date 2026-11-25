package com.sprint.mission.discodeit.domain.service.userstatus;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.repository.UserStatusRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserStatusServiceImpl implements UserStatusService{
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus createUserStatus(UserStatus userStatus) {

        if(userStatusRepository.findUserStatusByUserId(userStatus.getUserId()).isPresent()){
            log.info("해당 유저아이디를 필드로 갖는 개체가 존재함 들어온 유저아이디 : {}", userStatus.getUserId());
            throw new CustomException(CustomErrorCode.USER_STATUS_DUPLICATE);
        }

        return userStatusRepository.saveEntity(userStatus);
    }

    @Override
    public UserStatus findUserStatus(UUID userStatusId) {

        return userStatusRepository.findById(userStatusId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserStatus findUserStatusByUserId(UUID userId){
        return userStatusRepository.findUserStatusByUserId(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return userStatusRepository.findAllEntity();
    }

    @Override
    public UserStatus updateUserStatusByUserId(UUID userId) {

        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_STATUS_NOT_FOUND_BY_USER_ID));

        userStatus.activateUser();

        return userStatusRepository.saveEntity(userStatus);
    }

    /*
        8.23 활성화 시간을 인자로 주는거로 변경...
        todo : 이상함
     */
    @Override
    public UserStatus updateUserStatusByUserId(UUID userId, Instant activeAt){
        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_STATUS_NOT_FOUND_BY_USER_ID));

        userStatus.activateUser(activeAt);

        return userStatusRepository.saveEntity(userStatus);
    }

    @Override
    public void deleteUserStatusByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_STATUS_NOT_FOUND_BY_USER_ID));

        userStatusRepository.deleteEntity(userStatus.getId());
    }
}
