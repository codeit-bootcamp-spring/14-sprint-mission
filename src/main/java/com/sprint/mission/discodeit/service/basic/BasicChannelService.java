package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    ChannelRepository channelRepository;
    ReadStatusRepository readStatusRepository;
    MessageRepository messageRepository;

    @Override
    public ChannelResponse createPublic(ChannelCreatePublicRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        channelRepository.save(channel);
        return toResponse(channel);
    }

    @Override
    public ChannelResponse createPrivate(ChannelCreatePrivateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        if (request.userIds() != null){
            for (UUID userId : request.userIds()) {
                ReadStatus readStatus = new ReadStatus(userId, channel.getId());
                readStatusRepository.save(readStatus);
            }
        }

        return toResponse(channel);
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        return toResponse(channel);
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<UUID> myPrivateChannelIds = readStatusRepository.findByUserId(userId)
                .stream().map(ReadStatus::getChannelId)
                .toList();

        // 요구 사항: 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가
        // PUBLIC 채널 목록은 전체 조회
        // PRIVATE 채널은 조회한 User가 참여한 채널만 조회
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC
                        || myPrivateChannelIds.contains(channel.getId()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ChannelResponse update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE){
            throw new IllegalStateException("PRIVATE 채널은 정보를 수정할 수 없습니다.");
        }

        channel.update(request.newName(), request.newDescription());
        channelRepository.save(channel);
        return toResponse(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }

        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);

        channelRepository.deleteById(channelId);
    }

    private ChannelResponse toResponse(Channel channel) {
        Instant lastMessageAt = null;
        List<UUID> participatingUserIds = new ArrayList<>();

        // 해당 채널의 최근 매세지 시간 조회
        List<Message> messages = messageRepository.findByChannelId(channel.getId());
        if (messages != null && !messages.isEmpty()){
            lastMessageAt = messages.stream()
                    .map(Message::getCreatedAt)
                    .max(Instant::compareTo)
                    .orElse(null);
        }

        // PRIVATE 채널인 경우 participatingUserIds 조회
        if (channel.getType() == ChannelType.PRIVATE){
            List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel.getId());
            if (readStatuses != null){
                participatingUserIds = readStatuses.stream()
                        .map(ReadStatus::getUserId)
                        .toList();
            }
        }

        return new ChannelResponse(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                lastMessageAt,
                participatingUserIds
        );
    }
}
