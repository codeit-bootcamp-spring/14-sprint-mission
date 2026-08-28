package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService  implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;



    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        Channel entity = request.toEntity();

        channelRepository.save(entity);
        return ChannelResponseDto.from(entity, List.of(), null);
    }
    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request) {
        Channel channel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(channel);

        for (UUID userId : request.userIds()) {
            User user = userRepository.findById(userId);
            if (Objects.isNull(user)) {
                throw new IllegalArgumentException("존재하지 않는 유저입니다: " + userId);
            }

            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), Instant.now());
            readStatusRepository.save(readStatus);
        }
        return ChannelResponseDto.from(channel, request.userIds(), null);
    }

    @Override
    public ChannelResponseDto find(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (Objects.isNull(channel)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);

        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
            ? readStatusRepository.findAllByChannelId(channel.getId()).stream().map(ReadStatus::getUserId).toList()
            : List.of();

        return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
    }

    @Override
    public ChannelResponseDto update(UUID id, ChannelUpdateRequestDto updateRequest) {
        Channel channel = channelRepository.findById(id);
        if (Objects.isNull(channel)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        }
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new RuntimeException("PRIVATE 채널은 수정할 수 없습니다");
        }
        channel.update(updateRequest.name(), updateRequest.description());
        channelRepository.save(channel);

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);

        return ChannelResponseDto.from(channel,List.of(),lastMessageAt);


    }

    @Override
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (Objects.isNull(channel)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        }
        channelRepository.deleteById(id);

        List<Message> messages = messageRepository.findAllByChannelId(id);
        for (Message message : messages) {
            messageRepository.deleteById(message.getId());
        }


        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(id);
        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.deleteById(readStatus.getId());
        }

    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        // 1. 이 유저가 참여 중인 PRIVATE 채널들의 channelId 목록 구하기
        List<UUID> privateChannelIds = readStatusRepository.findAllByUserId(userId)
            .stream()
            .map(ReadStatus::getChannelId)
            .toList();

        // 2. 전체 채널 중에서, "PUBLIC이거나" 또는 "PRIVATE인데 위 목록에 포함된 것"만 필터링
        List<Channel> channels = channelRepository.findAll().stream()
            .filter(channel ->
                channel.getType() == ChannelType.PUBLIC
                    || privateChannelIds.contains(channel.getId())
            )
            .toList();

        // 3. 각 채널을 ChannelResponseDto로 변환 (lastMessageAt, participantIds 채우기)
        return channels.stream()
            .map(channel -> {
                Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(Message::getCreatedAt)
                    .max(Instant::compareTo)
                    .orElse(null);

                List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
                    ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList()
                    : List.of();

                return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
            })
            .toList();
    }
}
