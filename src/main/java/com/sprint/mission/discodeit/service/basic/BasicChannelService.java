package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto createPublic(PublicChannelCreateRequest request) {
        log.debug("PUBLIC 채널 생성 시작: name={}", request.name());
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        channelRepository.save(channel);
        log.info("PUBLIC 채널 생성 완료: channelId={}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
        log.debug("PRIVATE 채널 생성 시작: participantIds={}", request.participantIds());
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

        for (UUID userId : request.participantIds()) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
            readStatusRepository.save(new ReadStatus(user, channel, channel.getCreatedAt()));
        }
        log.info("PRIVATE 채널 생성 완료: channelId={}, 참여자 수={}", channel.getId(),
            request.participantIds().size());
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelMapper.toDto(getChannel(channelId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatus -> readStatus.getChannel().getId())
            .toList();

        return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, myChannelIds).stream()
            .map(channelMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.debug("채널 수정 시작: channelId={}", channelId);
        Channel channel = getChannel(channelId);
        channel.update(request.newName(), request.newDescription());
        log.info("채널 수정 완료: channelId={}", channelId);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.debug("채널 삭제 시작: channelId={}", channelId);
        Channel channel = getChannel(channelId);
        messageRepository.deleteAll(messageRepository.findAllByChannelId(channelId));
        channelRepository.delete(channel);
        log.info("채널 삭제 완료: channelId={}", channelId);
    }

    private Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }
}
