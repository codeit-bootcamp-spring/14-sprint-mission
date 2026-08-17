package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicReadStatusService(
            ReadStatusRepository readStatusRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository
    ) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        userRepository.findById(request.userId())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND,
                        "유저를 찾을 수 없습니다! id: " + request.userId()));
        channelRepository.findById(request.channelId())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.CHANNEL_NOT_FOUND,
                        "채널을 찾을 수 없습니다! id: " + request.channelId()));

        boolean alreadyExists = readStatusRepository.findAllByUserId(request.userId()).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(request.channelId()));
        if (alreadyExists) {
            throw new DiscodeitException(ExceptionType.READ_STATUS_ALREADY_EXISTS,
                    "이미 등록된 유저·채널 조합입니다! userId: " + request.userId() + ", channelId: " + request.channelId());
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
        return toDto(readStatusRepository.save(readStatus));
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return toDto(findEntity(readStatusId));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = findEntity(readStatusId);
        readStatus.updateLastReadAt(request.newLastReadAt());
        return toDto(readStatusRepository.save(readStatus));
    }

    @Override
    public void delete(UUID readStatusId) {
        findEntity(readStatusId);
        readStatusRepository.deleteById(readStatusId);
    }

    private ReadStatus findEntity(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.READ_STATUS_NOT_FOUND,
                        "읽음 상태를 찾을 수 없습니다! id: " + readStatusId));
    }

    private ReadStatusDto toDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }
}
