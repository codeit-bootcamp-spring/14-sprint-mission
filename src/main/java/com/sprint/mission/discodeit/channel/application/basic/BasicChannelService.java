package com.sprint.mission.discodeit.channel.application.basic;

import com.sprint.mission.discodeit.channel.application.ChannelService;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.common.entity.base.BaseEntity;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelDto publicCreate(PublicChannelCreateRequest request) {
        Channel channel = request.toEntity();

        return publicChannelCreate(channel);
    }

    @Override
    @Transactional
    public ChannelDto privateCreate(PrivateChannelCreateRequest request) {
        Channel channel = request.toEntity();

        return privateChannelCreate(channel, request.participantIds());

    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto find(UUID id) {
        Channel channel = channelCheck(id);

        List<UUID> userIds = new ArrayList<>();
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            userIds = readStatusRepository.findUserIdByChannelId(channel.getId());
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(BaseUpdatableEntity::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);

        List<User> users = userIds.stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(NoSuchElementException::new))
                .toList();

        return channelMapper.toDto(channel, users, lastMessageAt);
    }

    // 특정 유저가 볼 수 있는 채널 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.findAllVisibleTo(userId);
    }


    @Override
    @Transactional
    public ChannelDto update(UUID id, ChannelUpdateRequestDto request) {
        Channel channel = channelCheck(id);
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            throw new PrivateChannelUpdateNotAllowedException();
        }
        channel.update(request.newName(), request.newDescription());
//        channelRepository.save(channel1);  // 변경 감지

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(BaseEntity::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);


        return channelMapper.toDto(channel, List.of(), lastMessageAt);
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

    private ChannelDto publicChannelCreate(Channel channel) {

        channelRepository.save(channel);
        log.info("public 채널 생성 성공 - channelId = {}, name = {}", channel.getId(), channel.getName());
        return channelMapper.toDto(channel, List.of(), null);
    }


    private ChannelDto privateChannelCreate(Channel channel, List<UUID> userIds) {

        List<User> participants = new ArrayList<>();
        for (UUID userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> {
                log.warn("private 채널 생성 실패 - 존재하지 않는 사용자: userId = {}", userId);
                return new NoSuchElementException();
            });
            participants.add(user);

            ReadStatus readStatus = new ReadStatus(user, channel);

            // 여기 read statusRepository 에다가 넣기
            readStatusRepository.save(readStatus);
            log.debug("readStatus 생성 성공 - readStatusId = {}", readStatus.getId());
        }

        channelRepository.save(channel);
        log.info("privateChannel 생성 성공 - channelId = {}", channel.getId());
        return channelMapper.toDto(channel, participants, null);
    }


}
