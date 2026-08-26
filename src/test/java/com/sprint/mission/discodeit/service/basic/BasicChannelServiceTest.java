package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.channel.dto.*;
import com.sprint.mission.discodeit.channel.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.channel.application.basic.BasicChannelService;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.message.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.channel.application.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        ChannelResponseDto created = channelService.publicCreate(new PublicChannelCreateRequest( "제목", "메모 내용"));
        ChannelFindResponseDto channelResponseDto = channelService.find(created.id());

        assertEquals("제목", channelResponseDto.name());
        assertEquals("메모 내용", channelResponseDto.description());
    }

    @Test
    void PRIVATE_채널을_생성하면_유저ID들을_조회_할_수_있다() {
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.privateCreate(new PrivateChannelCreateRequest(userIds));

        ChannelFindResponseDto found = channelService.find(created.id());

        assertEquals(userIds.size(), found.participantIds().size());
        assertTrue(found.participantIds().containsAll(userIds));

    }

    @Test
    void 유저ID를_입력하면_사용하능한_채널을_조회한다() {
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created1 = channelService.privateCreate(new PrivateChannelCreateRequest(userIds));
        ChannelResponseDto created2 = channelService.publicCreate(new PublicChannelCreateRequest("제목", "메모 내용"));
        ChannelResponseDto created3 = channelService.privateCreate(new PrivateChannelCreateRequest(List.of(UUID.randomUUID())));

        List<UUID> created = List.of(new UUID[]{created1.id(), created2.id()});

        List<UUID> foundIds = channelService.findAllByUserId(userIds.get(0)).stream()
                .map(ChannelFindResponseDto::id)
                .toList();

        assertEquals(2, foundIds.size());
        assertTrue(foundIds.containsAll(created));

    }

    @Test
    void PRIVATE_채널을_생성하고_수정하면_예외가_발생한다(){
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.privateCreate(new PrivateChannelCreateRequest(userIds));

        ChannelUpdateRequestDto updated = new ChannelUpdateRequestDto(created.name(), created.description());

        assertThrows(PrivateChannelUpdateNotAllowedException.class, () -> channelService.update(created.id(), updated));

    }

    @Test
    void 채널을_삭제하면_메세지와_스테이터스도_삭제된다(){
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ChannelResponseDto created = channelService.privateCreate(new PrivateChannelCreateRequest(userIds));

        messageRepository.save(new Message(null,"첫 메시지", created.id(), userIds.get(0)));
        messageRepository.save(new Message(null,"둘째 메시지", created.id(), userIds.get(1)));

        channelService.delete(created.id());

        assertThrows(NoSuchElementException.class, () -> channelService.find(created.id()));
        assertTrue(messageRepository.findAllByChannelId(created.id()).isEmpty());
        assertTrue(readStatusRepository.findAllByChannelId(created.id()).isEmpty());
    }


}