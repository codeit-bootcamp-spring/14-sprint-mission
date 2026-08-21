package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponse create(UUID userId, UUID channelId, Instant lastReadAt) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND_IN_DATABASE));

        if (readStatusRepository.existsByUserAndChannel(user.getId(), channel.getId())) {
            throw new CustomException(ExceptionType.READSTATUS_ALREADY_EXISTS);
        }
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        ReadStatus created = readStatusRepository.create(readStatus);

        return ReadStatusResponse.of(created);
    }

    public ReadStatus getReadStatus(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.READSTATUS_NOT_FOUND_IN_DATABASE));
    }

    public List<ReadStatusResponse> getAllReadStatusByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatusResponse::of)
                .toList();
    }

    public ReadStatusResponse updateReadStatus(UUID publicReadStatusId, Instant newLastReadAt) {
        ReadStatus updated = readStatusRepository.update(publicReadStatusId, newLastReadAt);
        return ReadStatusResponse.of(updated);

    }

    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }

}
