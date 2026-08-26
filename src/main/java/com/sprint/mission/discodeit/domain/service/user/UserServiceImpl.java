package com.sprint.mission.discodeit.domain.service.user;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/*
    todo : 리팩토링
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        userRepository.findByEmail(user.getEmail())
            .ifPresent(email -> {
                throw new CustomException(CustomErrorCode.USER_DUPLICATE_EMAIL);
            });

        return userRepository.saveEntity(user);
    }

    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    //일단 이름만 변경 가능 하도록 설계
    @Override
    public User updateUser(UUID id, String name) {
        User user = this.findUserById(id);

       user.updateName(name);

        return userRepository.saveEntity(user);
    }

    // 8월 23일 추가
    @Override
    public User updateUser(UUID id, String name, String email, String password, UUID profileImageId){
        User user = this.findUserById(id);

        user.updateAllField(name, email, password);

        //todo : 업데이트 시 이미지 필드 수정 다시
        user.updateProfileImage(profileImageId);

        return userRepository.saveEntity(user);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAllEntity();
    }

    /*
        삭제메서드는 단독 호출로 불리지 않는다
     */
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteEntity(id);
    }

    @Override
    public boolean existAllByIdList(List<UUID> idList) {
        log.info("--------------- info {} ", idList);
        return userRepository.existAllById(idList);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(CustomErrorCode.USER_AUTH_MISMATCH));
    }



}
