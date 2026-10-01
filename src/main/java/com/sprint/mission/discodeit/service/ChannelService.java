package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    public ChannelDto createChannel(Channel.ChannelType type, String name, String description) {
        Channel channel = Channel.create(type, name, description);
        Channel savedChannel = channelRepository.save(channel);
        return channelMapper.toDto(savedChannel);
    }

    @Transactional
    public ChannelDto updateChannel(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        
        channel.update(newName, newDescription);
        return channelMapper.toDto(channel);
    }
}
