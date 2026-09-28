package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND,
                "해당 유저를 찾을 수 없습니다. userId: " + request.userId()));
        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND,
                "해당 채널을 찾을 수 없습니다. channelId: " + request.channelId()));
        if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_READ_STATUS);
        }

        Instant lastReadAt = request.lastReadAt() != null ? request.lastReadAt() : Instant.now();
        ReadStatus readStatus = readStatusRepository.save(
            new ReadStatus(user, channel, lastReadAt));
        return readStatusMapper.toDto(readStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusMapper.toDto(getReadStatus(readStatusId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = getReadStatus(readStatusId);
        readStatus.update(request.newLastReadAt());
        return readStatusMapper.toDto(readStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.delete(getReadStatus(readStatusId));
    }

    private ReadStatus getReadStatus(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND,
                "ReadStatus를 찾을 수 없습니다. id: " + readStatusId));
    }
}
