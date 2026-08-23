package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusApplication {
    private final ReadStatusService readStatusService;
    private final UserService userService;
    private final ChannelService channelService;

    public ReadStatusResponse create(UUID userId, UUID channelId, Instant lastReadAt) {
        User user = userService.findById(userId);
        Channel channel = channelService.findById(channelId);
        readStatusService.validateAlreadyExistsByUserAndChannel(user.getId(), channel.getId());
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        ReadStatus created = readStatusService.create(readStatus);
        return ReadStatusResponse.of(created);
    }

    public ReadStatus getReadStatus(UUID id) {
        return readStatusService.findById(id);
    }

    public List<ReadStatusResponse> getAllReadStatusByUserId(UUID userId) {
        userService.validateExistsById(userId);
        return readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatusResponse::of)
                .toList();
    }

    public ReadStatusResponse updateReadStatus(UUID publicReadStatusId, Instant newLastReadAt) {
        ReadStatus updated = readStatusService.update(publicReadStatusId, newLastReadAt);
        return ReadStatusResponse.of(updated);
    }

    public void deleteReadStatus(UUID id) {
        readStatusService.deleteById(id);
    }
}
