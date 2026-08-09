package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.MessageCodesResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicChannelServiceTest {

    private ChannelService channelService;
    private MessageRepository messageRepository;
    private ReadStatusRepository readStatusRepository;

    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();
        channelService = new BasicChannelService(
                new JCFChannelRepository(),
                readStatusRepository,
                messageRepository
        );

    }


    @Test
    void PUBLIC_채널을_생성하면_조회할_수_있다() {
        ChannelResponseDto created = channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "제목", "메모 내용", null));
        ChannelResponseDto channelResponseDto = channelService.find(created.id());

        assertEquals("제목", channelResponseDto.title());
        assertEquals("메모 내용", channelResponseDto.memo());
    }

    @Test
    void PRIVATE_채널을_생성하면_유저ID들을_조회_할_수_있다() {
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "제목", "메모 내용", userIds));

        ChannelResponseDto found = channelService.find(created.id());

        assertEquals(userIds.size(), found.userIds().size());
        assertTrue(found.userIds().containsAll(userIds));

    }

    @Test
    void 유저ID를_입력하면_사용하능한_채널을_조회한다() {
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created1 = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "제목", "메모 내용", userIds));
        ChannelResponseDto created2 = channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "제목", "메모 내용", null));
        ChannelResponseDto created3 = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "제목", "메모 내용", List.of(UUID.randomUUID())));

        List<UUID> created = List.of(new UUID[]{created1.id(), created2.id()});

        List<UUID> foundIds = channelService.findAllByUserId(userIds.get(0)).stream()
                .map(ChannelResponseDto::id)
                .toList();

        assertEquals(2, foundIds.size());
        assertTrue(foundIds.containsAll(created));

    }

    @Test
    void PRIVATE_채널을_생성하고_수정하면_예외가_발생한다(){
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "제목", "메모 내용", userIds));

        ChannelUpdateRequestDto updated = new ChannelUpdateRequestDto(created.id(), created.channelType(), created.title(), created.memo(),
                created.userIds());

        assertThrows(PrivateChannelUpdateNotAllowedException.class, () -> channelService.update(updated));

    }

    @Test
    void 채널을_삭제하면_메세지와_스테이터스도_삭제된다(){
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "제목", "메모 내용", userIds));

        messageRepository.save(new Message(null,"첫 메시지", created.id(), userIds.get(0)));
        messageRepository.save(new Message(null,"둘째 메시지", created.id(), userIds.get(1)));

        channelService.delete(created.id());

        assertThrows(NoSuchElementException.class, () -> channelService.find(created.id()));
        assertTrue(messageRepository.findAllByChannelId(created.id()).isEmpty());
        assertTrue(readStatusRepository.findAllByChannelId(created.id()).isEmpty());
    }


}