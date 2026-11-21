package com.sprint.mission.discodeit.readstatus.application;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.channel.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.service.ReadStatusService;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.readstatus.web.dto.req.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.readstatus.web.dto.res.ReadStatusResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReadStatusApplicationService {
    private final ReadStatusService readStatusService;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    private final ReadStatusMapper readStatusMapper;

    @Transactional
    public ReadStatusResponseDTO createReadStatus(ReadStatusCreateRequestDTO readStatusCreateRequestDTO){
        User user = userRepository.getByIdOrThrow(readStatusCreateRequestDTO.userId());
        Channel channel = channelRepository.getByIdOrThrow(readStatusCreateRequestDTO.channelId());
        ReadStatus readStatus = ReadStatus.init(user, channel, readStatusCreateRequestDTO.lastReadAt());

        ReadStatus createdReadStatus = readStatusService.createReadStatus(readStatus);

        return readStatusMapper.toResponse(createdReadStatus);
    }

    @Transactional
    public List<ReadStatusResponseDTO> findReadStatusByUserId(UUID userId) {

        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
            .map(readStatusMapper::toResponse)
            .toList();
    }

    @Transactional
    public ReadStatusResponseDTO updateReadStatusReadTime(UUID readStatusId, Instant updateTime) {

        ReadStatus readStatus = readStatusRepository.getByIdOrThrow(readStatusId);
        readStatus.updateReadTime(updateTime);

        return readStatusMapper.toResponse(readStatus);
    }
}
