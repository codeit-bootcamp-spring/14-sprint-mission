package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public ChannelServiceImpl(ChannelRepository channelRepository, UserRepository userRepository,
        MessageRepository messageRepository, ReadStatusRepository readStatusRepository,
        BinaryContentRepository binaryContentRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.readStatusRepository = readStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
    }


    @Override
    public ChannelResponseDto channelCreate(
        ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
        Channel channel = new Channel(channelPublicCreateRequestDto.channelName(),
            ChannelType.PUBLIC, channelPublicCreateRequestDto.description());
        channelRepository.channelAdd(channel);
        return toResponseDto(channel);
    }

    @Override
    public ChannelResponseDto privateChannelCreate(
        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {
        Channel channel = new Channel(ChannelType.PRIVATE);

        channelRepository.channelAdd(channel);

        for (UUID userId : channelPrivateCreateRequestDto.participantIds()) {
            User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저: " + userId));

            ReadStatus readStatus = new ReadStatus(channel.getChannelId(), user.getUserId(),
                Instant.now());
            readStatusRepository.statusAdd(readStatus);
        }

        return toResponseDto(channel);
    }

    @Override
    public void channelUpdate(UUID channelId, ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findByChannel(channelId)
            .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다: " + channelId));

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다: " + channelId);
        }

        channel.update(channelUpdateRequestDto.channelName(),
            channelUpdateRequestDto.description());
        channelRepository.update(channel);
    }

    @Override
    public ChannelResponseDto findById(UUID channelId) {
        Channel channel = channelRepository.findByChannel(channelId)
            .orElseThrow(() -> new IllegalArgumentException("보고자 하는 채널이 없습니다: " + channelId));
        return toResponseDto(channel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);

        List<UUID> myChannelIds = readStatusRepository.findByUserId(userId);
        List<Channel> myPrivateChannels = channelRepository.findAllByType(ChannelType.PRIVATE)
            .stream()
            .filter(channel -> myChannelIds.contains(channel.getChannelId()))
            .toList();

        return Stream.concat(publicChannels.stream(), myPrivateChannels.stream())
            .map(this::toResponseDto)
            .toList();
    }

    // 채널 -> DTO로 변환
    // find랑 findAll이랑 겹쳐서 통합 사용을 위해 생성
    private ChannelResponseDto toResponseDto(Channel channel) {
        List<Message> messages = messageRepository.findAllMessage(channel.getChannelId());

        Instant lastMessageAt = messages.stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            List<UUID> participantIds = readStatusRepository.findByChannelId(
                channel.getChannelId());
            return ChannelResponseDto.from(channel, lastMessageAt, participantIds);
        }

        return ChannelResponseDto.from(channel, lastMessageAt);
    }

    @Override
    public void channelDelete(UUID channelId) {
        Channel channel = channelRepository.findByChannel(channelId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다: " + channelId));

        List<UUID> attachmentIds = messageRepository.findAllMessage(channelId).stream()
            .map(Message::getBinaryContentsId)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .toList();

        attachmentIds.forEach(binaryContentRepository::delete);
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.delete(channel);
    }
}
