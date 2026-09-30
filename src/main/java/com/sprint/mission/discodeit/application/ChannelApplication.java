package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.user.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChannelApplication {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserService userService;
    private final ReadStatusService readStatusService;

    @Transactional
    public ChannelUpsertResponse createPublicChannel(String name, String description) {
        Channel creating = Channel.createPublicChannel(name, description);
        Channel created = channelService.create(creating);
        return ChannelUpsertResponse.of(created);
    }

    @Transactional
    public ChannelUpsertResponse createPrivateChannel(List<UUID> userIds) {
        List<User> users = userService.findByIds(userIds);
        Channel channel = Channel.createPrivateChannel(userIds);

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = ReadStatus.of(users, channel, Instant.now());
        readStatusService.createAll(readStatuses);

        Channel created = channelService.create(channel);
        return ChannelUpsertResponse.of(created);
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다.
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    @Transactional
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        userService.validateExistsById(userId);
        return findAccessibleChannels(userId).stream()
                .map(this::getChannelResponseDto)
                .toList();
    }

    private List<Channel> findAccessibleChannels(UUID userId) {
        return channelService.findAll().stream()
                .filter(channel -> isChannelAccessible(channel, userId))
                .toList();
    }

    private boolean isChannelAccessible(Channel channel, UUID userId) {
        return channel.isPublic() || readStatusService.existsByUserAndChannel(userId, channel.getId());
    }

    private ChannelResponseDto getChannelResponseDto(Channel channel) {
        List<UUID> participantIds = readStatusService.findAllUserIdsByChannelId(channel.getId());
        // 채널에 메세지가 없어도 정상 흐름이므로 Optional을 Application에서 해제함.
        //
        // 기존에는 마지막 메세지 시간을 channel.updatedAt으로 판단했으나,
        // 1. 메세지가 생성됐다고 channel의 updatedAt을 갱신하는게 타당한지 의문이고 (message가 연관관계의 주인)
        // 2. 요구사항에서 테이블 스키마를 고정해놔서 channel 내에 lastMessageAt 필드를 추가할 수 없어서
        // 최근 메세지를 직접 찾는 방식으로 변경했는데 이게 좋은 방식인지도 잘 모르곘음
        Instant lastMessageAt = messageService.findLastMessageAtByChannelId(channel.getId())
                .orElse(channel.getUpdatedAt());
        return ChannelResponseDto.of(channel, participantIds, lastMessageAt);
    }

    @Transactional
    public ChannelUpsertResponse updateChannelName(UUID id, String name, String description) {
        Channel updated = channelService.updateNameAndDescription(id, name, description);
        return ChannelUpsertResponse.of(updated);
    }

    @Transactional
    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageService.deleteAllByChannelId(id);
        readStatusService.deleteByChannelId(id);
        channelService.deleteById(id);
    }
}
