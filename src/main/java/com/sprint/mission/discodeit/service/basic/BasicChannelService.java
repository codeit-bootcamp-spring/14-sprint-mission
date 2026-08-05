package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public ChannelResponseDto getChannel(UUID id) {
        Channel channel = channelRepository.findById(id).orElseThrow(() -> new NoSuchElementException("니가 찾는 채널이 없다."));
        Instant messageLastSentAt = messageRepository.findAllByChannelId(id).stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<UUID> userIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUserId())
                .toList();

        return ChannelResponseDto.of(
                channel,
                messageLastSentAt,
                channel.getChannelType().equals(ChannelType.PRIVATE) ? userIds : null
        );
    }

    // 1. DTO를 활용해 가장 최근 메시지의 시간 정보 포함
    // 2. PRIVATE 채널인 경우 참여한 User의 id정보 반환
    // 1, 2 요구사항은 service의 getChannel 재사용

    // 3. 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
    // 4. PUBLIC인 전체조회, PRIVATE은 User가 참여한 채널만 조회하도록
    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<ChannelResponseDto> getAllChannelsByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllByUserId(userId);

        return channels.stream()
                .map(channel -> this.getChannel(channel.getId()))
                .toList();
    }

    // TODO 1. DTO 활용해 파라미터 그룹화
    // TODO 2. PRIVATE 채널은 수정할 수 없음
    @Override
    public void updateChannelName(UUID id, ChannelUpdateNameDto dto) {
        channelRepository.updateName(id, dto.getName());
    }

    @Override
    public void deleteChannel(UUID id) {
        // 채널 내부 message 삭제
        messageRepository.deleteAllByChannelId(id);
        channelRepository.deleteById(id);
        readStatusRepository.deleteByChannelId(id);
    }
}
