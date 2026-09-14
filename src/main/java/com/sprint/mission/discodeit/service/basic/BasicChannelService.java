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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService  implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        Channel channel = request.toEntity();
        channelRepository.save(channel);
        return ChannelResponseDto.from(channel, List.of(), null);
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request) {
        Channel channel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(channel);

        for (UUID userId : request.participantIds()) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + userId));

            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            readStatusRepository.save(readStatus);
        }

        return ChannelResponseDto.from(channel, request.participantIds(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

        Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
            .map(Message::getCreatedAt)
            .orElse(null);

        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
            ? readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
            .map(rs -> rs.getUser().getId())
            .toList()
            : List.of();

        return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
    }

    @Override
    public ChannelResponseDto update(UUID id, ChannelUpdateRequestDto updateRequest) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(updateRequest.newName(), updateRequest.newDescription());


        Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
            .map(Message::getCreatedAt)
            .orElse(null);

        return ChannelResponseDto.from(channel, List.of(), lastMessageAt);
    }

    @Override
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

        channelRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<UUID> privateChannelIds = readStatusRepository.findAllByUser_Id(userId)
            .stream()
            .map(rs -> rs.getChannel().getId())
            .toList();

        List<Channel> channels = channelRepository.findAll().stream()
            .filter(channel ->
                channel.getType() == ChannelType.PUBLIC
                    || privateChannelIds.contains(channel.getId())
            )
            .toList();

        return channels.stream()
            .map(channel -> {
                Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
                    .map(Message::getCreatedAt)
                    .orElse(null);

                List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
                    ? readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
                    .map(rs -> rs.getUser().getId())
                    .toList()
                    : List.of();

                return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
            })
            .toList();
    }
}
