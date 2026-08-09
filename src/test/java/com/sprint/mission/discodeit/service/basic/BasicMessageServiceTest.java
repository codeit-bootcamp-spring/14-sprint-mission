package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicMessageServiceTest {

    private MessageService messageService;
    private MessageRepository messageRepository;
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        messageService = new BasicMessageService(
                messageRepository,
                new JCFChannelRepository(),
                new JCFUserRepository(),
                binaryContentRepository
        );
    }


    @Test
    void 메시지를_생성하면_채널별_조회에_포함되어_있다() {
        UUID channelA = UUID.randomUUID();
        UUID channelB = UUID.randomUUID();
        UUID author = UUID.randomUUID();

        messageService.create(new MessageCreateRequestDto(List.of(), "첫 번째", channelA, author));
        messageService.create(new MessageCreateRequestDto(List.of(), "두 번째", channelA, author));
        messageService.create(new MessageCreateRequestDto(List.of(), "다른 채널", channelB, author));

        List<MessageResponseDto> found = messageService.findAllByChannelId(channelA);

        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(m -> m.message().equals("첫 번째")));
        assertTrue(found.stream().anyMatch(m -> m.message().equals("두 번째")));
    }

    @Test
    void 메시지를_생성하면_id로_조회할_수_있다() {
        UUID channelId = UUID.randomUUID();
        UUID author = UUID.randomUUID();

        messageService.create(new MessageCreateRequestDto(List.of(), "안녕하세요", channelId, author));
        MessageResponseDto response = messageService.findAllByChannelId(channelId).stream().findFirst().orElseThrow();

        MessageResponseDto found = messageService.find(response.id());

        assertEquals("안녕하세요", found.message());
        assertEquals(channelId, found.channelId());
        assertEquals(author, found.userId());
    }

    @Test
    void 없는_id로_조회하면_예외가_발생한다() {
        assertThrows(NoSuchElementException.class, () -> messageService.find(UUID.randomUUID()));
    }

    @Test
    void 메시지를_수정하면_같은_id로_조회했을_때_내용이_바뀐다() {
        UUID channelId = UUID.randomUUID();
        UUID author = UUID.randomUUID();

        messageService.create(new MessageCreateRequestDto(List.of(), "수정 전", channelId, author));
        MessageResponseDto saved = messageService.findAllByChannelId(channelId).stream().findFirst().orElseThrow();

        messageService.update(new MessageUpdateRequestDto(
                saved.id(), "수정 후", channelId, author, List.of()));

        // 반환값이 아니라 원래 id로 다시 조회해서 검증한다
        MessageResponseDto found = messageService.find(saved.id());
        assertEquals("수정 후", found.message());

        // 수정은 새 메시지를 만드는 게 아니므로 전체 개수는 그대로여야 한다
        assertEquals(1, messageRepository.findAll().size());
    }

    @Test
    void 메시지를_삭제하면_조회할_수_없고_첨부파일도_함께_삭제된다() {
        UUID channelId = UUID.randomUUID();
        UUID author = UUID.randomUUID();

        BinaryContent attachment = new BinaryContent(new byte[]{1, 2, 3});
        binaryContentRepository.save(attachment);

        messageService.create(new MessageCreateRequestDto(
                List.of(attachment.getId()), "삭제될 메시지", channelId, author));
        MessageResponseDto saved = messageService.findAllByChannelId(channelId).stream().findFirst().orElseThrow();

        messageService.delete(saved.id());

        assertThrows(NoSuchElementException.class, () -> messageService.find(saved.id()));
        assertTrue(binaryContentRepository.findById(attachment.getId()).isEmpty());
    }

    @Test
    void 첨부파일이_null인_메시지도_삭제할_수_있다() {
        UUID channelId = UUID.randomUUID();
        UUID author = UUID.randomUUID();

        messageService.create(new MessageCreateRequestDto(null, "첨부 없음", channelId, author));
        MessageResponseDto saved = messageService.findAllByChannelId(channelId).stream().findFirst().orElseThrow();

        assertDoesNotThrow(() -> messageService.delete(saved.id()));
    }
}
