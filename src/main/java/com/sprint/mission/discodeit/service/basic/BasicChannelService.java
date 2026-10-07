package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
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
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    //
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.debug("Public 채널 생성 상세 요청 데이터: {}", request);

        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);

        log.info("Public 채널 생성 완료 - 채널 ID: {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.debug("Private 채널 생성 상세 요청 데이터: {}", request);
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds())
            .stream()
            .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
            .toList();
        readStatusRepository.saveAll(readStatuses);

        log.info("Private 채널 생성 완료 - 채널 ID: {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
            .map(channelMapper::toDto)
            .orElseThrow(
                () -> new ChannelNotFoundException(channelId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .map(Channel::getId)
            .toList();

        return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
            .stream()
            .map(channelMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.debug("채널 수정 상세 요청 - 대상 ID: {}, 데이터: {}", channelId, request);
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("채널 수정 실패 - 존재하지 않는 채널 ID: {}", channelId);
                return new ChannelNotFoundException(channelId);
            });

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            log.warn("채널 수정 실패 - Private 채널은 수정 불가: {}", channelId);
            throw new PrivateChannelUpdateException(channelId);
        }
        channel.update(newName, newDescription);

        log.info("채널 수정 완료 - 채널 ID: {}", channelId);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 삭제 실패 - 존재하지 않는 채널 ID: {}", channelId);
            throw new ChannelNotFoundException(channelId);
        }

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.deleteById(channelId);
        log.info("채널 삭제 완료 - 채널 ID: {}", channelId);
    }
}
