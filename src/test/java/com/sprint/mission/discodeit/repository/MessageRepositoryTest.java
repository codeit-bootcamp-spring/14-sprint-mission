package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  private Channel channel;
  private User author;

  @BeforeEach
  void setUp() {
    author = new User("author", "author@example.com", "password", null);
    new UserStatus(author, Instant.now());
    em.persist(author);
    channel = em.persist(new Channel(ChannelType.PUBLIC, "general", ""));
  }

  private List<Message> saveMessages(int count) {
    List<Message> messages = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      messages.add(em.persist(new Message("message " + i, channel, author, List.of())));
    }
    em.flush();
    em.clear();
    return messages;
  }

  @Test
  @DisplayName("커서 이전 메시지를 createdAt 내림차순으로 페이지 크기만큼 조회한다")
  void findAllByChannelIdWithAuthor_paging() {
    saveMessages(3);
    PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        Instant.now().plusSeconds(1), pageable);

    assertThat(slice.getContent()).hasSize(2);
    assertThat(slice.hasNext()).isTrue();
    assertThat(slice.getContent())
        .extracting(Message::getCreatedAt)
        .isSortedAccordingTo(Comparator.reverseOrder());
    assertThat(slice.getContent().get(0).getAuthor().getUsername()).isEqualTo("author");
  }

  @Test
  @DisplayName("다른 채널이거나 커서 이후의 메시지는 조회하지 않는다")
  void findAllByChannelIdWithAuthor_empty() {
    saveMessages(2);
    PageRequest pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

    Slice<Message> otherChannel = messageRepository.findAllByChannelIdWithAuthor(UUID.randomUUID(),
        Instant.now().plusSeconds(1), pageable);
    Slice<Message> beforeAll = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        Instant.EPOCH, pageable);

    assertThat(otherChannel.getContent()).isEmpty();
    assertThat(beforeAll.getContent()).isEmpty();
    assertThat(beforeAll.hasNext()).isFalse();
  }

  @Test
  @DisplayName("채널의 마지막 메시지 시각을 조회한다")
  void findLastMessageAtByChannelId_success() {
    List<Message> messages = saveMessages(3);
    Instant latest = messages.stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElseThrow();

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    assertThat(lastMessageAt).contains(latest);
  }

  @Test
  @DisplayName("메시지가 없는 채널이면 빈 Optional을 반환한다")
  void findLastMessageAtByChannelId_empty() {
    assertThat(messageRepository.findLastMessageAtByChannelId(channel.getId())).isEmpty();
  }
}
