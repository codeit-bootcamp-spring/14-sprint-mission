package com.sprint.mission.discodeit.core.application.channel.service;

import com.sprint.mission.discodeit.core.application.channel.port.in.ChannelUseCase;
import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelResult;
import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelUpsertResult;
import com.sprint.mission.discodeit.core.application.channel.port.out.ChannelPort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.user.User;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.core.application.message.port.out.MessagePort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.service.AuthService;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@ApplicationService
@RequiredArgsConstructor
public class ChannelApplication implements ChannelUseCase {
    private final ChannelPort channels;
    private final MessagePort messages;
    private final UserPort users;
    private final AuthService authService;
    private final ReadStatusPort readStatuses;

    @Override
    @MutationLog
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    public ChannelUpsertResult createPublicChannel(String name, String description) {
        Channel creating = Channel.createPublicChannel(name, description);
        Channel created = channels.save(creating);
        return ChannelUpsertResult.from(created);
    }

    @Override
    @MutationLog
    @Transactional
    public ChannelUpsertResult createPrivateChannel(List<UUID> userIds) {
        List<User> participants = users.findByIdIn(userIds);
        Channel creatingChannel = Channel.createPrivateChannel(userIds);
        Channel createdChannel = channels.save(creatingChannel);

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> creatingReadStatuses = ReadStatus.of(participants, createdChannel, Instant.now());
        readStatuses.saveAll(creatingReadStatuses);
        return ChannelUpsertResult.from(createdChannel);
    }

    @Override
    @Transactional
    public List<ChannelResult> getAllChannelsByUserId(UUID userId) {
        List<Channel> accessibleChannels = channels.findAccessibleByUserId(userId);
        List<UUID> channelIds = accessibleChannels.stream()
                .map(Channel::getId)
                .toList();
        Map<UUID, Instant> lastMessageAtByChannelId = messages.findLastMessageAtByChannelId(channelIds);

        return accessibleChannels.stream()
                .map(channel -> toResult(channel, lastMessageAtByChannelId))
                .toList();
    }

    private ChannelResult toResult(Channel channel, Map<UUID, Instant> channelLastMessageAt) {
        List<UserResult> participants = channel.isPrivate()
                ? channel.getParticipants().stream()
                        .map(user -> UserResult.from(user, authService.isOnline(user)))
                        .toList()
                : null;
        return ChannelResult.from(
                channel,
                participants,
                channelLastMessageAt.getOrDefault(channel.getId(), channel.getUpdatedAt())
        );
    }

    @Override
    @MutationLog
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    public ChannelUpsertResult updateChannelName(UUID id, String name, String description) {
        Channel updated = channels.get(id).updateNameAndDescription(name, description);
        return ChannelUpsertResult.from(updated);
    }

    @Override
    @MutationLog
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        Channel deleting = channels.get(id);
        messages.deleteAllByChannelId(id);
        readStatuses.deleteAllByChannelId(id);
        channels.delete(deleting);
    }
}
