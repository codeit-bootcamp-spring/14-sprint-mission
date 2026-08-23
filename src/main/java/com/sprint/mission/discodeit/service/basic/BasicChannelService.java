package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResponseDto createPublicChannel(ChannelCreateRequestDto requestDto) {
//        if (requestDto.getMemberIds() != null) {
//            for (UUID memberId : requestDto.getMemberIds()) {
//                if (Objects.isNull(userService.readUser(memberId))) {
//                    throw new RuntimeException("존재하지 않는 유저: " + memberId);
//                }
//            }
//        }
        if (requestDto.getMemberIds() != null) {
            requestDto.getMemberIds().stream()
                    .filter(each -> Objects.isNull(userService.readUser(each)))
                    .findFirst()
                    .ifPresent(each -> {
                        // throw new RuntimeException("존재하지 않는 유저입니다: ");
                        throw new CustomRuntimeException(ExceptionType.USER_NOT_FOUND, each);
                    });
        }
        List<UUID> memberIdList = requestDto.getMemberIds() == null ?
                new ArrayList<>() : new ArrayList<>(requestDto.getMemberIds());
        Channel channel = requestDto.toEntity();
        channelRepository.save(channel);
        return ChannelResponseDto.from(channel);
    }

    @Override
    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        return createPublicChannel(requestDto);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        if (requestDto.getMemberIds() != null) {
            requestDto.getMemberIds().stream()
                    .filter(each -> Objects.isNull(userService.readUser(each)))
                    .findFirst()
                    .ifPresent(each -> {
                        // throw new RuntimeException("존재하지 않는 유저입니다: ");
                        throw new CustomRuntimeException(ExceptionType.USER_NOT_FOUND, each);
                    });
        }

        Channel privateChannel = requestDto.toEntity();
        channelRepository.save(privateChannel);
        // 참여한 유저별로 readStatus생성
        if (requestDto.getMemberIds() != null) {
            requestDto.getMemberIds().stream()
                    .forEach(each -> {
                        ReadStatus readStatus = new ReadStatus(each, privateChannel.getId());
                        readStatusRepository.save(readStatus);
                    });
        }
        return ChannelResponseDto.from(privateChannel);
    }

    @Override
    public ChannelResponseDto readChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (Objects.isNull(channel)) {
            // throw new RuntimeException("해당 채널이 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.CHANNEL_NOT_FOUND, id);
        }
        Instant lastMessageAt = getLastMessageAt(id);
        return ChannelResponseDto.from(channel, lastMessageAt);
    }

    @Override
    public List<ChannelResponseDto> readAllChannel() {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponseDto> responses = new ArrayList<>();
        for (Channel channel : channels) {
            Instant lastMessageAt = getLastMessageAt(channel.getId());
            responses.add(ChannelResponseDto.from(channel, lastMessageAt));
        }
        return responses;
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponseDto> responses = new ArrayList<>();
        for (Channel channel : channels) {
            if (channel.getChannelType() == ChannelType.PUBLIC ||
                    (channel.getMemberIds() != null && channel.getMemberIds().contains(userId))) {
                Instant lastMessageAt = getLastMessageAt(channel.getId());
                responses.add(ChannelResponseDto.from(channel, lastMessageAt));
            }
        }
        return responses;
    }

    @Override
    public ChannelResponseDto updateChannel(UUID id, ChannelUpdateRequestDto requestDto) {
        Channel target = channelRepository.findById(id);
        if (Objects.isNull(target)) {
            // throw new RuntimeException("해당 채널이 존재하지 않습니다.");
            throw new CustomRuntimeException(ExceptionType.CHANNEL_NOT_FOUND, id);
        }
        if (target.getChannelType() == ChannelType.PRIVATE) {
            // throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
            throw new CustomRuntimeException(ExceptionType.PRIVATE_CHANNEL_CANNOT_BE_UPDATED);
        }
        if (Objects.isNull(requestDto.getChannelName()) || requestDto.getChannelName().isBlank()) {
            // throw new RuntimeException("유효하지 않은 채널명 입니다.");
            throw new CustomRuntimeException(ExceptionType.BAD_REQUEST);
        }
        target.setChannelName(requestDto.getChannelName());

        if (requestDto.getMemberIds() != null && !requestDto.getMemberIds().isEmpty()) {
            for (UUID memberId : requestDto.getMemberIds()) {
                if (Objects.isNull(userService.readUser(memberId))) {
                    // throw new RuntimeException("존재하지 않는 유저입니다: " + memberId);
                    throw new CustomRuntimeException(ExceptionType.USER_NOT_FOUND, memberId);
                }
            }
            target.setMemberIds(new ArrayList<>(requestDto.getMemberIds()));
        }
        target.setUpdatedAt();
        channelRepository.save(target);
        Instant lastMessageAt = getLastMessageAt(id);
        return ChannelResponseDto.from(target, lastMessageAt);
    }

    @Override
    public void deleteChannel(UUID id) {
        List<Message> messages = messageRepository.findAllByChannelId(id);
        for (Message message : messages) {
            messageRepository.delete(message.getId());
        }
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(id);
        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.delete(readStatus.getId());
        }
        channelRepository.delete(id);
    }

    private Instant getLastMessageAt(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
    }
}
