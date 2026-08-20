package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.IReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService implements IReadStatusService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto request) {
        ReadStatus readStatus = request.toEntity();

        Channel channel = channelRepository.findById(readStatus.getChannelId());
        if (Objects.isNull(channel)) {
            throw new RuntimeException("존재하지 않는 채널 입니다");
        }
        User user = userRepository.findById(readStatus.getUserId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("존재하지 않는 유저아이디 입니다");
        }
        if (readStatusRepository.existsByUserIdAndChannelId(user.getId(),channel.getId())) {
            throw new RuntimeException("이미 존재하는 읽음 상태입니다");
        }
        readStatusRepository.save(readStatus);


        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public ReadStatusResponseDto find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (Objects.isNull(readStatus)) {
            throw new RuntimeException("없는 읽음 상태입니다");
        }

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
            .map(ReadStatusResponseDto::from)
            .toList();
    }

    @Override
    public ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto request) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (Objects.isNull(readStatus)) {
            throw new RuntimeException("존재하지 않는 읽음 상태입니다");
        }
        readStatus.update(request.lastReadAt());
        readStatusRepository.save(readStatus);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (Objects.isNull(readStatus)) {
            throw new RuntimeException("존재하지 않는 읽음 상태입니다");
        }
        readStatusRepository.deleteById(id);
    }
}
