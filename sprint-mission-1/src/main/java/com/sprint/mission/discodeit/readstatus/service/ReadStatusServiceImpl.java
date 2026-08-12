package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.exception.NotFoundException;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusResponseDto readStatusCreate(
        ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        channelRepository.findByChannel(readStatusCreateRequestDto.channelId())
            .orElseThrow(() -> NotFoundException.channel(readStatusCreateRequestDto.channelId()));

        userRepository.findByUser(readStatusCreateRequestDto.userId())
            .orElseThrow(() -> NotFoundException.user(readStatusCreateRequestDto.userId()));

        return ReadStatusResponseDto.from(readStatusRepository.statusAdd(
            new ReadStatus(readStatusCreateRequestDto.channelId(),
                readStatusCreateRequestDto.userId(), Instant.now())));
    }

    @Override
    public ReadStatusResponseDto readStatusUpdate(UUID readStatusId,
        ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);

        if (readStatusUpdateRequestDto.lastReadAt() != null) {
            readStatus.updateAt(readStatusUpdateRequestDto.lastReadAt());
        }

        readStatusRepository.update(readStatus);

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public void readStatusDelete(UUID readStatusId) {
        readStatusRepository.delete(readStatusId);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByAllUserList(userId);

        return readStatuses.stream()
            .map(ReadStatusResponseDto::from)
            .toList();
    }

    @Override
    public ReadStatusResponseDto findReadStatus(UUID readStatusId) {
        return ReadStatusResponseDto.from(readStatusRepository.findById(readStatusId));
    }
}
