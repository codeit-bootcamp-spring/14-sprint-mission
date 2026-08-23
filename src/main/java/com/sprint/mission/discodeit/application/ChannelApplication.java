package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.dto.channel.ChannelUpsertResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChannelApplication {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserService userService;
    private final ReadStatusService readStatusService;

    public ChannelUpsertResponse createPublicChannel(String name, String description) {
        Channel channel = Channel.createPublicChannel(name, description);
        Channel created = channelService.create(channel);
        return ChannelUpsertResponse.of(created);
    }

    public ChannelUpsertResponse createPrivateChannel(List<UUID> userIds) {
        userService.validateAllExists(userIds);

        Channel channel = Channel.createPrivateChannel(userIds);
        UUID channelId = channel.getId();

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = userIds.stream()
                .map(userId -> new ReadStatus(userId, channelId, null))
                .toList();
        readStatusService.createAll(readStatuses);

        Channel created = channelService.create(channel);
        return ChannelUpsertResponse.of(created);
    }

    public ChannelResponseDto getChannel(UUID id) {
        Channel channel = channelService.findById(id);
        List<UUID> participantIds = readStatusService.findAllUserIdsByChannelId(channel.getId());
        return ChannelResponseDto.of(channel, participantIds);
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다.
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        userService.validateExistsById(userId);
        return channelService.findAll().stream()
                .filter(channel -> channel.isPublic()
                        || readStatusService.existsByUserAndChannel(userId, channel.getId()))
                .map(channel -> {
                    List<UUID> participantIds = readStatusService.findAllUserIdsByChannelId(channel.getId());
                    return ChannelResponseDto.of(channel, participantIds);
                }).toList();
    }

    public ChannelUpsertResponse updateChannelName(UUID id,
                                                String name, String description) {
        Channel updated = channelService.updateNameAndDescription(id, name, description);
        return ChannelUpsertResponse.of(updated);
    }

    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageService.deleteAllByChannelId(id);
        readStatusService.deleteByChannelId(id);
        Channel deleted = channelService.deleteById(id);
    }
}
