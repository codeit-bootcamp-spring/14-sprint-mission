package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.core.application.message.port.out.MessagePort;
import com.sprint.mission.discodeit.core.application.message.service.MessageApplication;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.message.Message;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageApplicationTest {
    @Mock
    private MessagePort messages;

    @InjectMocks
    private MessageApplication application;

    @Test
    void anotherUserCannotUpdateMessage() {
        User author = User.createUser("author", "author@example.com", "password", null);
        ReflectionTestUtils.setField(author, "id", UUID.randomUUID());
        Message message = Message.of("original", author, Channel.createPublicChannel("public", null), List.of());
        UUID messageId = UUID.randomUUID();
        ReflectionTestUtils.setField(message, "id", messageId);
        when(messages.get(messageId)).thenReturn(message);

        assertThatThrownBy(() -> application.updateMessage(messageId, "changed", UUID.randomUUID()))
                .isInstanceOf(AccessDeniedException.class);
        assertThat(message.getContent()).isEqualTo("original");
    }
}
