package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createChannel(ChannelCreationDto dto) {
        Channel channel = new Channel(dto.getTitle(), dto.getUsersId());
        return channelRepository.create(channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        return channelRepository.findById(uuid);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannelName(UUID id, ChannelUpdateNameDto dto) {
        channelRepository.updateName(id, dto);
    }

    @Override
    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        channelRepository.findById(id)
                        .ifPresent(channel -> channel.getMessagesId().forEach(messageRepository::deleteById));

        channelRepository.deleteById(id);
    }
}
