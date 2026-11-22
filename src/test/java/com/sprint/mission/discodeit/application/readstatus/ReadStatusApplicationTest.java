package com.sprint.mission.discodeit.application.readstatus;

import com.sprint.mission.discodeit.core.application.channel.port.out.ChannelPort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.readstatus.service.ReadStatusApplication;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusException;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusExceptionType;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ReadStatusApplicationTest {
    @Mock
    private ReadStatusPort readStatuses;
    @Mock
    private UserPort users;
    @Mock
    private ChannelPort channels;
    @InjectMocks
    private ReadStatusApplication application;

    @Test
    void duplicateCheckUsesChannelId() {
        User user = User.createUser("name", "name@example.com", "password", null);
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        ReflectionTestUtils.setField(user, "id", userId);
        Channel channel = Channel.createPrivateChannel(List.of(userId));
        ReflectionTestUtils.setField(channel, "id", channelId);
        when(users.get(userId)).thenReturn(user);
        when(channels.get(channelId)).thenReturn(channel);
        when(readStatuses.existsByUserAndChannel(userId, channelId)).thenReturn(true);

        assertThatThrownBy(() -> application.create(userId, channelId, Instant.now()))
                .isInstanceOfSatisfying(ReadStatusException.class, exception -> {
                    assertThat(exception.getType()).isEqualTo(ReadStatusExceptionType.READ_STATUS_ALREADY_EXISTS);
                    assertThat(exception.getDetails()).containsEntry("channelId", channelId);
                });
        verify(readStatuses, never()).save(any());
    }
}
