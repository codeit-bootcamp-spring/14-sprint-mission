package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
class DiscodeitWorkflowIntegrationTests {

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private BinaryContentStorage binaryContentStorage;

  @Test
  void createsAndReadsUsersMessagesFilesAndCursorPages() throws IOException {
    UserDto author = userService.create(
        new UserCreateRequest("author", "author@example.com", "password"),
        Optional.of(new BinaryContentCreateRequest("profile.txt", "text/plain", "profile".getBytes()))
    );
    UserDto removableUser = userService.create(
        new UserCreateRequest("removable", "removable@example.com", "password"),
        Optional.empty()
    );

    assertThat(userService.find(author.id()).online()).isTrue();
    assertThat(userService.update(
        author.id(),
        new UserUpdateRequest(author.username(), author.email(), null),
        Optional.empty()
    ).username()).isEqualTo("author");

    var channel = channelService.create(new PublicChannelCreateRequest("general", "General chat"));
    MessageDto firstMessage = messageService.create(
        new MessageCreateRequest("first", channel.id(), author.id()),
        List.of(new BinaryContentCreateRequest("note.txt", "text/plain", "attachment".getBytes()))
    );
    MessageDto secondMessage = messageService.create(
        new MessageCreateRequest("second", channel.id(), author.id()),
        List.of()
    );

    PageResponse<MessageDto> firstPage = messageService.findAllByChannelId(
        channel.id(),
        null,
        PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"))
    );
    assertThat(firstPage.content()).hasSize(1);
    assertThat(firstPage.hasNext()).isTrue();
    assertThat(firstPage.nextCursor()).isInstanceOf(Instant.class);

    PageResponse<MessageDto> secondPage = messageService.findAllByChannelId(
        channel.id(),
        (Instant) firstPage.nextCursor(),
        PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"))
    );
    assertThat(secondPage.content()).hasSize(1);
    assertThat(secondPage.content().get(0).id())
        .isNotEqualTo(firstPage.content().get(0).id());
    assertThat(List.of(firstMessage.id(), secondMessage.id()))
        .contains(firstPage.content().get(0).id(), secondPage.content().get(0).id());

    try (InputStream stream = binaryContentStorage.get(firstMessage.attachments().get(0).id())) {
      assertThat(stream.readAllBytes()).isEqualTo("attachment".getBytes());
    }

    userService.delete(removableUser.id());
    assertThatThrownBy(() -> userService.find(removableUser.id()))
        .isInstanceOf(NoSuchElementException.class);
  }
}
