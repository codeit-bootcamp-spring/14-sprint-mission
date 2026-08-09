package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateStatus;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserStatusServiceTest {

    private UserStatusService userStatusService;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        userStatusService = new BasicUserStatusService(userRepository, userStatusRepository);

        byte[] image = {1, 2, 3, 4};
        User user = new User(new UserRequestDto("김양현", "yyy2724@naver.com", "2724", image));
        userRepository.save(user);
        userId = user.getId();
    }

    @Test
    void 유저상태를_생성하면_저장된다() {
        UserStatusResponseDto created = userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));

        assertEquals(userId, created.userId());
        assertNotNull(created.lastAccessAt());
        assertTrue(userStatusRepository.findByUserId(userId).isPresent());
    }

    @Test
    void 없는_유저로_생성하면_예외가_발생한다() {
        assertThrows(NotFoundUserException.class,
                () -> userStatusService.create(new UserStatusCreateRequestDto(UUID.randomUUID(), Instant.now())));
    }

    @Test
    void 같은_유저로_중복_생성하면_예외가_발생한다() {
        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));

        assertThrows(DuplicateStatus.class,
                () -> userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now())));
    }

    @Test
    void 유저상태를_생성하면_조회할_수_있다() {
        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));
        UUID id = userStatusRepository.findByUserId(userId).orElseThrow().getId();

        UserStatusResponseDto found = userStatusService.find(id);

        assertEquals(userId, found.userId());
        assertNotNull(found.lastAccessAt());
    }

    @Test
    void 없는_유저상태를_조회하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class, () -> userStatusService.find(UUID.randomUUID()));
    }

    @Test
    void 모든_유저상태를_조회한다() {
        byte[] image = {1, 2, 3, 4};
        User user2 = new User(new UserRequestDto("홍길동", "hong@naver.com", "1234", image));
        userRepository.save(user2);

        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));
        userStatusService.create(new UserStatusCreateRequestDto(user2.getId(), Instant.now()));

        List<UserStatusResponseDto> found = userStatusService.findAll();

        assertEquals(2, found.size());
    }

    @Test
    void 유저상태를_수정하면_마지막_접속시간이_갱신된다() {
        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow();
        Instant before = userStatus.getLastAccessAt();

        UserStatusResponseDto updated = userStatusService.update(new UserStatusUpdateRequestDto(userStatus.getId()));

        assertEquals(userId, updated.userId());
        assertFalse(updated.lastAccessAt().isBefore(before));
        assertNotNull(userStatusRepository.findById(userStatus.getId()).orElseThrow().getUpdatedAt());
    }

    @Test
    void 없는_유저상태를_수정하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class,
                () -> userStatusService.update(new UserStatusUpdateRequestDto(UUID.randomUUID())));
    }

    @Test
    void 유저ID로_유저상태를_수정할_수_있다() {
        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));

        UserStatusResponseDto updated = userStatusService.updateByUserId(userId);

        assertEquals(userId, updated.userId());
        assertNotNull(updated.lastAccessAt());
    }

    @Test
    void 없는_유저ID로_수정하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class, () -> userStatusService.updateByUserId(UUID.randomUUID()));
    }

    @Test
    void 유저상태를_삭제하면_조회할_수_없다() {
        userStatusService.create(new UserStatusCreateRequestDto(userId, Instant.now()));
        UUID id = userStatusRepository.findByUserId(userId).orElseThrow().getId();

        userStatusService.delete(new UserStatusUpdateRequestDto(id));

        assertTrue(userStatusRepository.findById(id).isEmpty());
    }

    @Test
    void 없는_유저상태를_삭제하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class,
                () -> userStatusService.delete(new UserStatusUpdateRequestDto(UUID.randomUUID())));
    }
}
