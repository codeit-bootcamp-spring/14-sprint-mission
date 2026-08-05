package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus create(@Valid ReadStatusCreateDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow();
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow();

        if (readStatusRepository.existsByUserAndChannel(user.getId(), channel.getId())) {
            throw new IllegalArgumentException("ReadStatus 이미 있어서 못만듦 ㅋ");
        }

        return dto.toReadStatus();
    }

    public ReadStatus getReadStatus(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow();
    }

    public List<ReadStatus> getAllReadStatusByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }




}
