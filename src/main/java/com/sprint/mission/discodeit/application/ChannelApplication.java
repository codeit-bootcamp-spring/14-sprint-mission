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
        Channel creatingChannel = Channel.createPrivateChannel(userIds);
        Channel createdChannel = channelService.create(creatingChannel);

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = ReadStatus.of(users, createdChannel, Instant.now());
        readStatusService.createAll(readStatuses);
        return ChannelUpsertResponse.of(createdChannel);
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다.
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    @Transactional
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        userService.validateExistsById(userId);
        return channelService.findAccessibleByUserId(userId).stream()
                .map(this::getChannelResponseDto)
                .toList();
    }

    private ChannelResponseDto getChannelResponseDto(Channel channel) {
        // Join해서 한 번에 가져오면 ReadStatus Domain Service에서 User 뱉는게 마음에 안듦
        // 그렇다고 계층 지킨답시고 쿼리 두 번 날리는 것도 마음에 안듦
        // 일단 그냥 쿼리 두 번 함
        // ManyToMany는 안티패턴?
        // 이게 마음에 안들면 JPA 쓰지마셈 ㅇㅇ 딴지 걸면 할 말은 없음
        List<UUID> participantIds = readStatusService.findAllUserIdsByChannelId(channel.getId());
        List<User> participants = userService.findByIds(participantIds);
        // 채널에 메세지가 없어도 정상 흐름이므로 Optional을 Application에서 해제함.
        //
        // 기존에는 마지막 메세지 시간을 channel.updatedAt으로 판단했으나,
        // 1. 메세지가 생성됐다고 channel의 updatedAt을 갱신하는게 타당한지 의문이고 (message가 연관관계의 주인)
        // 2. 요구사항에서 테이블 스키마를 고정해놔서 channel 내에 lastMessageAt 필드를 추가할 수 없어서
        // 최근 메세지를 직접 찾는 방식으로 변경했는데 이게 좋은 방식인지도 잘 모르곘음
        Instant lastMessageAt = messageService.findLastMessageAtByChannelId(channel.getId())
                .orElse(channel.getUpdatedAt());
        return ChannelResponseDto.of(channel, participants, lastMessageAt);
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
