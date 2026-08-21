package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto requestDto) {
        if (Objects.isNull(userRepository.findById(requestDto.getUserId()))
                || Objects.isNull(channelRepository.findById(requestDto.getChannelId()))) {
            // throw new RuntimeException("관련된 Channel이나 User가 존재하지 않습니다.");
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }

        boolean exists = readStatusRepository.findAll().stream()
                .anyMatch(each -> Objects.equals(each.getUserId(), requestDto.getUserId())
                        && Objects.equals(each.getChannelId(), requestDto.getChannelId()));
        if (exists) {
            // throw new RuntimeException("해당 채널과 유저에 대한 ReadStatus가 이미 존재합니다.");
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }

        ReadStatus readStatus = requestDto.toEntity();
        readStatusRepository.save(readStatus);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public ReadStatusResponseDto readReadStatus(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (Objects.isNull(readStatus)) {
            // throw new RuntimeException("해당 ReadStatus가 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(each -> ReadStatusResponseDto.from(each))
                .toList();
    }

    @Override
    public ReadStatusResponseDto updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto) {
        ReadStatus target = readStatusRepository.findById(id);
        if (Objects.isNull(target)) {
            // throw new RuntimeException("해당 ReadStatus가 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        target.setLastReadAt(requestDto.getLastReadAt());
        target.setUpdatedAt();
        readStatusRepository.save(target);
        return ReadStatusResponseDto.from(target);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.delete(id);
    }
}
