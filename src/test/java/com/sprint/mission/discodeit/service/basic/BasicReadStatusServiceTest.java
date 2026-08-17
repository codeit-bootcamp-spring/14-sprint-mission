package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readStatus.application.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.user.dto.UserRequestDto;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.common.exception.DuplicateStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.common.exception.NotFoundUserException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.channel.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.readStatus.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.readStatus.application.ReadStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicReadStatusServiceTest {

    private ReadStatusService readStatusService;
    private ReadStatusRepository readStatusRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;

    private UUID userId;
    private UUID channelId;

    @BeforeEach
    void setUp() {
        readStatusRepository = new JCFReadStatusRepository();
        userRepository = new JCFUserRepository();
        channelRepository = new JCFChannelRepository();
        readStatusService = new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);

        byte[] image = {1, 2, 3, 4};
        User user = new User(new UserRequestDto("김양현", "yyy2724@naver.com", "2724", image));
        userRepository.save(user);
        userId = user.getId();

        Channel channel = new Channel(ChannelType.PUBLIC, "제목", "메모 내용");
        channelRepository.save(channel);
        channelId = channel.getId();
    }

    @Test
    void 읽음상태를_생성하면_저장된다() {
        ReadStatusResponseDto created = readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));

        assertEquals(channelId, created.channelId());
        assertEquals(userId, created.userId());
        assertTrue(readStatusRepository.existsByChannelIdAndUserId(channelId, userId));
    }

    @Test
    void 없는_채널로_생성하면_예외가_발생한다() {
        assertThrows(NotFoundChannelException.class,
                () -> readStatusService.create(new ReadStatusCreateRequestDto(userId, UUID.randomUUID())));
    }

    @Test
    void 없는_유저로_생성하면_예외가_발생한다() {
        assertThrows(NotFoundUserException.class,
                () -> readStatusService.create(new ReadStatusCreateRequestDto(UUID.randomUUID(), channelId)));
    }

    @Test
    void 같은_채널과_유저로_중복_생성하면_예외가_발생한다() {
        readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));

        assertThrows(DuplicateStatus.class,
                () -> readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId)));
    }

    @Test
    void 읽음상태를_생성하면_조회할_수_있다() {
        readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));
        UUID id = readStatusRepository.findAllByUserId(userId).get(0).getId();

        ReadStatusResponseDto found = readStatusService.find(id);

        assertEquals(channelId, found.channelId());
        assertEquals(userId, found.userId());
    }

    @Test
    void 없는_읽음상태를_조회하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class, () -> readStatusService.find(UUID.randomUUID()));
    }

    @Test
    void 유저ID로_모든_읽음상태를_조회한다() {
        Channel channel2 = new Channel(ChannelType.PUBLIC, "제목2", "메모 내용2");
        channelRepository.save(channel2);

        readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));
        readStatusService.create(new ReadStatusCreateRequestDto(userId, channel2.getId()));

        List<ReadStatusResponseDto> found = readStatusService.findAllByUserId(userId);

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(dto -> dto.userId().equals(userId)));
    }

    @Test
    void 읽음상태를_수정하면_마지막_읽은_시간이_갱신된다() {
        readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));
        ReadStatus readStatus = readStatusRepository.findAllByUserId(userId).get(0);
        assertNull(readStatus.getLastReadTime());

        readStatusService.update(new ReadStatusUpdateRequestDto(readStatus.getId(), Instant.now()));

        ReadStatus updated = readStatusRepository.findById(readStatus.getId()).orElseThrow();
        assertNotNull(updated.getLastReadTime());
    }

    @Test
    void 없는_읽음상태를_수정하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class,
                () -> readStatusService.update(new ReadStatusUpdateRequestDto(UUID.randomUUID(), Instant.now())));
    }

    @Test
    void 읽음상태를_삭제하면_조회할_수_없다() {
        readStatusService.create(new ReadStatusCreateRequestDto(userId, channelId));
        UUID id = readStatusRepository.findAllByUserId(userId).get(0).getId();

        readStatusService.delete(id);

        assertTrue(readStatusRepository.findById(id).isEmpty());
    }

    @Test
    void 없는_읽음상태를_삭제하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class, () -> readStatusService.delete(UUID.randomUUID()));
    }
}
