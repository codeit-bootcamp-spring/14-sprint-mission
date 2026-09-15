package com.sprint.mission.discodeit.channel.application.basic;

import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.*;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.channel.application.ChannelService;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChannelResponseDto publicCreate(PublicChannelCreateRequest request) {
        Channel channel = request.toEntity();
        return publicChannelCreate(channel);
    }

    @Override
    @Transactional
    public ChannelResponseDto privateCreate(PrivateChannelCreateRequest request) {
        Channel channel = request.toEntity();
        return privateChannelCreate(channel, request.participantIds());

    }

    @Override
    @Transactional(readOnly = true)
    public ChannelFindResponseDto find(UUID id) {
        Channel channel = channelCheck(id);

        List<UUID> userIds = new ArrayList<>();
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            userIds = readStatusRepository.findAllByChannelId(channel.getId());
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(BaseUpdatableEntity::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        return ChannelFindResponseDto.from(channel.getId(),channel.getChannelType(),  channel.getName(), channel.getDescription(),
                userIds, lastMessageAt);
    }

    // 특정 유저가 볼 수 있는 채널 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChannelFindResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC ||
                        readStatusRepository.findAllByChannelId(channel.getId()).contains(userId))
                .map(channel -> find(channel.getId()))
                .toList();
    }


    @Override
    @Transactional
    public ChannelResponseDto update(UUID id, ChannelUpdateRequestDto request) {
        Channel channel1 = channelCheck(id);
        if (channel1.getChannelType() == ChannelType.PRIVATE) {
            throw new PrivateChannelUpdateNotAllowedException();
        }
        channel1.update(request.newName(), request.newDescription());
//        channelRepository.save(channel1);  // 변경 감지
        return ChannelResponseDto.from(channel1.getId(), channel1.getCreatedAt(), channel1.getUpdatedAt(),
                channel1.getChannelType(), channel1.getName(),
                channel1.getDescription());
    }

    @Override
    @Transactional
    public void delete(UUID id) {

//        messageRepository.deleteByChannelId(id);  cascade 처리
//        readStatusRepository.deleteByChannelId(id);
        channelRepository.deleteById(id);
    }


    private Channel channelCheck(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private ChannelResponseDto publicChannelCreate(Channel channel) {

        channelRepository.save(channel);
        return ChannelResponseDto.from(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getChannelType(), channel.getName(), channel.getDescription());
    }


    private ChannelResponseDto privateChannelCreate(Channel channel, List<UUID> userIds) {

        for (UUID userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            ReadStatus readStatus = new ReadStatus(user, channel);

            // 여기 read statusRepository 에다가 넣기
            readStatusRepository.save(readStatus);
        }

        channelRepository.save(channel);
        return ChannelResponseDto.from(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getChannelType(), channel.getName(), channel.getDescription());
    }


}
