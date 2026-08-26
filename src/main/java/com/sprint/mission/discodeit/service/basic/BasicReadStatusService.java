package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {

        userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND,
                "해당 유저 없음" + request.userId()));
        channelRepository.findById(request.channelId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND,
                "해당 채널 없음" + request.channelId()));

        List<ReadStatus> existing =
            readStatusRepository.findAllByUserId(request.userId());

        for (ReadStatus readStatus : existing) {
            if (readStatus.getChannelId().equals(request.channelId())) {
                throw new DiscodeitException(ErrorCode.DUPLICATE_READ_STATUS);
            }
        }

        ReadStatus readStatus = ReadStatus.builder()
            .userId(request.userId())
            .channelId(request.channelId())
            .build();
        readStatusRepository.save(readStatus);
        return toDto(readStatus);
    }

    @Override
    public Optional<ReadStatusDto> find(UUID id) {
        return readStatusRepository.findById(id)
            .map(BasicReadStatusService::toDto);

    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses =
            readStatusRepository.findAllByUserId(userId);

        List<ReadStatusDto> result = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            result.add(toDto(readStatus));
        }
        return result;
    }

    @Override
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(
                ErrorCode.READ_STATUS_NOT_FOUND,
                "ReadStatus를 찾을 수 없습니다." + id));

        readStatus.updateLastReadAt(request.lastReadAt());
        readStatusRepository.update(readStatus);

        return toDto(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.findById(id)
            .orElseThrow(() ->
                new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND,
                    "ReadStatus를 찾을 수 없습니다. id:" + id));
        readStatusRepository.delete(id);
    }


    private static ReadStatusDto toDto(ReadStatus status) {
        return new ReadStatusDto(
            status.getId(),
            status.getUserId(),
            status.getChannelId(),
            status.getLastReadAt()
        );


    }
}
