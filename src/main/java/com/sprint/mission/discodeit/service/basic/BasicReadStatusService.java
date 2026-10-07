package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        User user = userRepository.findById(userId)
            .orElseThrow(
                () -> new UserNotFoundException(userId));
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                () -> new ChannelNotFoundException(channelId)
            );

        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(user.getId(),
                channel.getId())
            .orElseGet(() -> {
                Instant lastReadAt = request.lastReadAt();
                return readStatusRepository.save(new ReadStatus(user, channel, lastReadAt));
            });

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
            .map(readStatusMapper::toDto)
            .orElseThrow(
                () -> new ReadStatusNotFoundException(readStatusId));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(
                () -> {
                    log.warn("읽음 상태 수정 실패 - 존재하지 않는 상태 ID: {}", readStatusId);
                    return new ReadStatusNotFoundException(readStatusId);
                });
        readStatus.update(newLastReadAt);
        return readStatusMapper.toDto(readStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            log.warn("읽음 상태 삭제 실패 - 존재하지 않는 상태 ID: {}", readStatusId);
            throw new ReadStatusNotFoundException(readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
