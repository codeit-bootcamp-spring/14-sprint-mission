package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Channel createChannel(ChannelCreationDto dto) {
        if (containsInvalidUser(dto.getUsersId())) {
            throw new IllegalArgumentException("repository에 존재하지 않는 User는 사용할 수 없습니다");
        }

        Channel channel = new Channel(dto.getTitle(), dto.getUsersId());
        return channelRepository.create(channel);
    }

    private boolean containsInvalidUser(List<UUID> userIds) {
        return !userIds.stream()
                .allMatch(userRepository::existsById);
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
        channelRepository.updateName(id, dto.getName());
    }

    @Override
    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageRepository.deleteAllByChannelId(id);
        channelRepository.deleteById(id);
    }
}
