package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    public Channel create(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel findById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND_IN_DATABASE));
    }

    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    public void deleteById(UUID id) {
        Channel deleting = findById(id);
        channelRepository.delete(deleting);
    }

    public Channel updateNameAndDescription(UUID id, String name, String description) {
        Channel updating = findById(id);
        return updating.updateNameAndDescription(name, description);
    }

    public void validateExists(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new CustomException(ExceptionType.CHANNEL_NOT_FOUND_IN_DATABASE);
        }
    }
}
