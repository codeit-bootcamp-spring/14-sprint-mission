package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JCFReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStausResponse create(ReadStatusCreateRequest readStatusCreateRequest) {
        userRepository.findById(readStatusCreateRequest.userId()).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        channelRepository.findChannel(readStatusCreateRequest.channelId()).orElseThrow(() -> new NoSuchElementException("해당 채널이 존재하지 않습니다."));
        ReadStatus readStatus = ReadStatus.create(readStatusCreateRequest.userId(),readStatusCreateRequest.channelId());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
        return ReadStausResponse.from(savedReadStatus);
    }

    @Override
    public ReadStausResponse findById(UUID uuid) {
        ReadStatus readStatus = readStatusRepository.find(uuid).orElseThrow(
            () -> new IllegalArgumentException("잘못된 요청입니다."));
        return ReadStausResponse.from(readStatus);
    }

    @Override
    public List<ReadStausResponse> findByUserId(UUID userId) {
        return readStatusRepository.findByUser(userId).stream().map(ReadStausResponse::from).toList();
    }

    @Override
    public void delete(UUID uuid) {
        readStatusRepository.delete(uuid);
    }

    @Override
    public ReadStausResponse update(ReadStausRequest readStausRequest) {
        ReadStatus readStatus = readStatusRepository.find(readStausRequest.uuid()).orElseThrow(() -> new NoSuchElementException("잘못된 요청입니다."));
        ReadStatus updatedReadStatus = readStatus.update(readStausRequest.recentReadAt());
        ReadStatus savedReadStatus = readStatusRepository.save(updatedReadStatus);
        return ReadStausResponse.from(savedReadStatus);
    }
}
