package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    public ReadStatusDto markAsRead(UUID userId, UUID channelId) {
        Optional<ReadStatus> optionalStatus = readStatusRepository.findByUserIdAndChannelId(userId,
            channelId);

        if (optionalStatus.isPresent()) {
            ReadStatus existingStatus = optionalStatus.get();
            existingStatus.updateLastReadAt();
            return readStatusMapper.toDto(existingStatus);
        } else {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
            Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

            ReadStatus readStatus = ReadStatus.create(user, channel);
            ReadStatus savedStatus = readStatusRepository.save(readStatus);
            return readStatusMapper.toDto(savedStatus);
        }
    }
}
