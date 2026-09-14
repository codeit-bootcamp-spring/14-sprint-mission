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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadStatusService implements IReadStatusService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto request) {
        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        if (readStatusRepository.existsByUser_IdAndChannel_Id(user.getId(), channel.getId())) {
            throw new IllegalArgumentException("이미 존재하는 읽음 상태입니다.");
        }

        ReadStatus readStatus = request.toEntity(user, channel);
        readStatusRepository.save(readStatus);

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusResponseDto find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("없는 읽음 상태입니다."));

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUser_Id(userId);
        return readStatuses.stream()
            .map(ReadStatusResponseDto::from)
            .toList();
    }

    @Override
    public ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto request) {
        ReadStatus readStatus = readStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 읽음 상태입니다."));

        readStatus.update(request.newLastReadAt());
        // save() 불필요 - 변경 감지로 자동 반영

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 읽음 상태입니다."));

        readStatusRepository.deleteById(id);
    }
}
