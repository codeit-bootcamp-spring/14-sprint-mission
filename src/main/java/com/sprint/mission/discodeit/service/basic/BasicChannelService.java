package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    private ChannelResponseDto createChannelResponseDto(Channel channel) {
        UUID channelId = channel.getId();
        Instant messageLastSentAt = messageRepository.findLatestMessageByChannelId(channelId)
                .orElse(null);
        List<UUID> userIds = readStatusRepository.findAllUserIdsByChannelId(channel.getId());

        return ChannelResponseDto.of(
                channel,
                messageLastSentAt,
                isChannelPrivate(channel) ? userIds : null
        );
    }

    private boolean isChannelPrivate(Channel channel) {
        return channel.getChannelType().equals(ChannelType.PRIVATE);
    }

    private boolean isChannelPublic(Channel channel) {
        return channel.getChannelType().equals(ChannelType.PUBLIC);
    }

    public ChannelResponseDto createChannel(ChannelType channelType,
                                            String name,
                                            List<UUID> userIds) {
        Channel channel = new Channel(channelType, name);
        UUID channelId = channel.getId();

        if (!userRepository.existsAllByIds(userIds)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }

        // 참여 User의 정보를 받아 User 별 ReadStatus 정보 생성
        List<ReadStatus> readStatuses = userIds.stream()
                .map(userId -> new ReadStatus(userId, channelId))
                .toList();
        readStatusRepository.createAll(readStatuses);

        Channel created = channelRepository.create(channel);
        return createChannelResponseDto(created);
    }

    public ChannelResponseDto getChannel(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND_IN_DATABASE));

        return createChannelResponseDto(channel);
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 1, 2 요구사항은 service의 getChannel 재사용

    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        // userId로 readStatus에서 channel 찾아서 반환
        // public은 전부 포함해야 함. private은 소속된 채널만
        return channelRepository.findAll().stream()
                .filter(channel -> isChannelPublic(channel)
                        || readStatusRepository.existsByUserAndChannel(userId, channel.getId()))
                .map(this::createChannelResponseDto)
                .toList();
    }

    // TODO 1. DTO 활용해 파라미터 그룹화
    // TODO 2. PRIVATE 채널은 수정할 수 없음
    public ChannelResponseDto updateChannelName(UUID id, String name) {
        Channel updated = channelRepository.updateName(id, name);
        return createChannelResponseDto(updated);
    }

    public ChannelResponseDto deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageRepository.deleteAllByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        Channel deleted = channelRepository.deleteById(id);
        return createChannelResponseDto(deleted);
    }
}
