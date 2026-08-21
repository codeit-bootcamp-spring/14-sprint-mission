package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChannelApplication {
    private final ChannelService channelService;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ReadStatusRepository readStatusRepository;

    public ChannelResponseDto createPublicChannel(String name, String description) {
        Channel channel = Channel.createPublicChannel(name, description);
        Channel created = channelService.create(channel);
        return ChannelResponseDto.of(created);
    }

    public ChannelResponseDto createPrivateChannel(List<UUID> userIds) {
        userService.validateAllExists(userIds);

        Channel channel = Channel.createPrivateChannel(userIds);
        UUID channelId = channel.getId();

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = userIds.stream()
                .map(userId -> new ReadStatus(userId, channelId, null))
                .toList();
        readStatusRepository.createAll(readStatuses);

        Channel created = channelService.create(channel);
        return ChannelResponseDto.of(created);
    }

    public ChannelResponseDto getChannel(UUID id) {
        Channel channel = channelService.findById(id);
        return ChannelResponseDto.of(channel);
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다.
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        return channelService.findAll().stream()
                .filter(channel -> isChannelPublic(channel)
                        || readStatusRepository.existsByUserAndChannel(userId, channel.getId()))
                .map(ChannelResponseDto::of)
                .toList();
    }

    private boolean isChannelPublic(Channel channel) {
        return channel.getChannelType().equals(ChannelType.PUBLIC);
    }

    public ChannelResponseDto updateChannelName(UUID id,
                                                String name, String description) {
        Channel updated = channelService.updateNameAndDescription(id, name, description);
        return ChannelResponseDto.of(updated);
    }

    public ChannelResponseDto deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageRepository.deleteAllByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        Channel deleted = channelService.deleteById(id);
        return ChannelResponseDto.of(deleted);
    }
}
