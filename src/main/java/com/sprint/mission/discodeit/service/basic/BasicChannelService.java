package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createChannel(ChannelCreationDto dto) {
        // 1. PUBLIC 채널 생성은 기존 로직 유지
        if (containsInvalidUser(dto.getUserIds())) {
            throw new IllegalArgumentException("repository에 존재하지 않는 User는 사용할 수 없습니다");
        }
        Channel channel = dto.toChannel();
        UUID channelId = channel.getId();
        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = dto.getUserIds().stream()
                .map(userId -> new ReadStatus(userId, channelId))
                .toList();

        readStatusRepository.createAll(readStatuses);
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
