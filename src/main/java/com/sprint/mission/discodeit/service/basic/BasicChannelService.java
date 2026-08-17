package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    public BasicChannelService(
            ChannelRepository channelRepository,
            UserRepository userRepository,
            ReadStatusRepository readStatusRepository,
            MessageRepository messageRepository,
            BinaryContentRepository binaryContentRepository
    ) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.readStatusRepository = readStatusRepository;
        this.messageRepository = messageRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public ChannelDto createPublic(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.channelName(), request.description());
        return toDto(channelRepository.save(channel));
    }

    @Override
    public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
        List<UUID> participantIds = request.participantIds();
        if (participantIds == null || participantIds.isEmpty()) {
            throw new DiscodeitException(ExceptionType.PRIVATE_CHANNEL_REQUIRES_PARTICIPANTS);
        }
        for (UUID participantId : participantIds) {
            userRepository.findById(participantId)
                    .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND,
                            "유저를 찾을 수 없습니다! id: " + participantId));
        }

        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
        for (UUID participantId : participantIds) {
            readStatusRepository.save(new ReadStatus(participantId, channel.getId(), Instant.now()));
        }
        return toDto(channel);
    }

    @Override
    public ChannelDto find(UUID channelId) {
        return toDto(findEntity(channelId));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        Set<UUID> joinedPrivateChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toCollection(HashSet::new));

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC
                        || joinedPrivateChannelIds.contains(channel.getId()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = findEntity(channelId);
        channel.update(request.newChannelName(), request.newDescription());
        return toDto(channelRepository.save(channel));
    }

    @Override
    public void delete(UUID channelId) {
        findEntity(channelId);

        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        for (Message message : messages) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
            messageRepository.deleteById(message.getId());
        }
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.deleteById(channelId);
    }

    private Channel findEntity(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.CHANNEL_NOT_FOUND,
                        "채널을 찾을 수 없습니다! id: " + channelId));
    }

    private ChannelDto toDto(Channel channel) {
        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
                        .map(ReadStatus::getUserId)
                        .toList()
                : List.of();

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        return new ChannelDto(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getChannelName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }
}
