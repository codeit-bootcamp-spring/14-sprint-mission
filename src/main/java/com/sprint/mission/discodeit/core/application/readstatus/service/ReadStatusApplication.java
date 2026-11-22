package com.sprint.mission.discodeit.core.application.readstatus.service;

import com.sprint.mission.discodeit.core.application.channel.port.out.ChannelPort;
import com.sprint.mission.discodeit.core.application.readstatus.port.in.ReadStatusUseCase;
import com.sprint.mission.discodeit.core.application.readstatus.port.in.dto.response.ReadStatusResult;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusException;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusExceptionType;
import com.sprint.mission.discodeit.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class ReadStatusApplication implements ReadStatusUseCase {
    private final ReadStatusPort readStatuses;
    private final UserPort users;
    private final ChannelPort channels;

    @Override
    @MutationLog
    @Transactional
    public ReadStatusResult create(UUID userId, UUID channelId, Instant lastReadAt) {
        User user = users.get(userId);
        Channel channel = channels.get(channelId);
        channel.validateReadStatusAllowed();

        ReadStatus readStatus = ReadStatus.of(user, channel, lastReadAt);
        validateAvailable(readStatus);
        return ReadStatusResult.from(readStatuses.save(readStatus));
    }

    @Override
    @Transactional
    public List<ReadStatusResult> getAllReadStatusByUserId(UUID userId) {
        return readStatuses.findAllByUserId(userId).stream()
                .map(ReadStatusResult::from)
                .toList();
    }

    @Override
    @MutationLog
    @Transactional
    public ReadStatusResult updateReadStatus(UUID readStatusId, Instant newLastReadAt) {
        ReadStatus updated = readStatuses.get(readStatusId).update(newLastReadAt);
        return ReadStatusResult.from(updated);
    }

    private void validateAvailable(ReadStatus readStatus) {
        UUID userId = readStatus.getUserId();
        UUID channelId = readStatus.getChannelId();
        if (readStatuses.existsByUserAndChannel(userId, channelId)) {
            throw new ReadStatusException(
                    ReadStatusExceptionType.READ_STATUS_ALREADY_EXISTS,
                    Map.of("userId", userId, "channelId", channelId)
            );
        }
    }
}
