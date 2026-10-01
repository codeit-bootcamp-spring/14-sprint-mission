package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel entity) {
        if (entity == null) {
            return null;
        }

        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(entity.getId());
        List<UserDto> participants = readStatuses.stream()
            .map(ReadStatus::getUser)
            .map(userMapper::toDto)
            .toList();

        Message lastMessage = messageRepository.findByChannelIdOrderByCreatedAtDesc(entity.getId())
            .orElse(null);
        Instant lastMessageAt = (lastMessage != null) ? lastMessage.getCreatedAt() : null;

        return new ChannelDto(
            entity.getId(),
            entity.getType(),
            entity.getName(),
            entity.getDescription(),
            participants,
            lastMessageAt
        );
    }
}
