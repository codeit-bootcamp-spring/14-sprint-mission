package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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



    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        Channel entity = request.toEntity();
        if(channelRepository.findById(entity.getId()) != null){
            throw new RuntimeException("이미 존재하는 채널입니다.");
        }
        channelRepository.save(entity);
        return ChannelResponseDto.from(entity, List.of(), null);
    }
    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request) {
        Channel channel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(channel);

        for (UUID userId : request.userIds()) {
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

        Instant lastMessageAt = messageRepository.findAll()
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
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }
}
