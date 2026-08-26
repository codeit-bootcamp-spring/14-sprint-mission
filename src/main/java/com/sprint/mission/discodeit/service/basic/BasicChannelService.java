package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelDto createPublic(PublicChannelCreateRequest request) {
        Channel channel = Channel.builder()
            .type(ChannelType.PUBLIC)
            .channelName(request.name())
            .description(request.description())
            .build();
        channelRepository.save(channel);
        return toDto(channel);
    }

    @Override
    public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
        Channel channel = Channel.builder()
            .type(ChannelType.PRIVATE)
            .channelName(null)
            .description(null)
            .build();
        channelRepository.save(channel);


        for (UUID userId : request.participantIds()) {
            ReadStatus readStatus = ReadStatus.builder()
                .userId(userId)
                .channelId(channel.getId())
                .lastReadAt(Instant.now())
                .build();
            readStatusRepository.save(readStatus);
        }
        return toDto(channel);
    }

    @Override
    public Optional<ChannelDto> find(UUID id) {
        return channelRepository.findById(id)
            .map(this::toDto);

    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository
            .findAllByType(ChannelType.PUBLIC);

        List<UUID> myPrivateChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannelId)
            .toList();

        List<Channel> myPrivateChannels = myPrivateChannelIds.stream()
            .map(channelRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(channel -> channel.getType() == ChannelType.PRIVATE)
            .toList();
        return Stream.concat(publicChannels.stream(),
                myPrivateChannels.stream()).map(this::toDto)
            .toList();
    }

    @Override
    public ChannelDto update(UUID id, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND,
                "해당 채널은 없습니다. 채널ID:" + id + ")"));

        channel.update(request.name(), request.description());

        channelRepository.update(channel);
        return toDto(channel);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND,
                "해당 채널을 찾을 수가 없습니다. 채널ID:" + id + ")"));


        messageRepository.findAllByChannelId(id)
            .forEach(message -> messageRepository.delete(message.getId()));

        readStatusRepository.findAllByChannelId(id)
            .forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));

        channelRepository.delete(id);
    }


    private ChannelDto toDto(Channel channel) {


        Instant lastMessageAt = messageRepository
            .findAllByChannelId(channel.getId())
            .stream()
            .map(Message::getCreatedAt)
            .max(Comparator.naturalOrder())
            .orElse(null);

        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
            ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
            .map(ReadStatus::getUserId)
            .toList() : List.of();

        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getChannelName(),
            channel.getDescription(),
            participantIds,
            lastMessageAt
        );

    }

}
