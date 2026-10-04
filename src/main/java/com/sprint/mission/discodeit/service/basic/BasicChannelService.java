package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.debug("(공개)채널 생성 시작 - 채널명: {}, 채널 타입: {}", request.name(),ChannelType.PUBLIC);
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    Channel newChannel = channelRepository.save(channel);
    log.debug("(공개)채널 생성 완료 - 채널아이디: {}, 채널명: {}", newChannel.getId(), newChannel.getName());
    return toDto(newChannel);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("(비공개)채널 생성 시작 - 채널타입: {}", ChannelType.PRIVATE);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);
    log.debug("(비공개)채널 생성 요청 - 채널아이디: {}, 채널타입: {}", createdChannel.getId(), ChannelType.PRIVATE);

//    request.participantIds().stream()
//        .map(userId -> new ReadStatus(userId, createdChannel.getId(), channel.getCreatedAt()))
//        .forEach(readStatusRepository::save);

    request.participantIds().stream()
        .map(userId -> new ReadStatus(
            userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)),
            createdChannel, channel.getCreatedAt()
        ))
        .forEach(readStatusRepository::save);

    return toDto(createdChannel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(
            () -> new ChannelNotFoundException(channelId));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.debug("채널 업데이트 시작 - 채널아이디: {}", channelId);
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.warn("존재하지 않는 채널 - 채널아이디: {}", channelId);
              return new ChannelNotFoundException(channelId);
            });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("수정 채널이 비공개 채널일경우 - 채널아이디: {}, 채널 타입: {}", channelId, ChannelType.PRIVATE);
      throw new PrivateChannelUpdateException(channelId);
    }
    channel.update(newName, newDescription);
    log.debug("채널 업데이트 요청 - 채널아이디: {}", channel.getId());
    return toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    log.debug("채널 삭제 시작 - 채널아이디: {}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.warn("존재하지 않는 채널 조회 - 채널아이디: {}", channelId);
              return new ChannelNotFoundException(channelId);
            });

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
    //DONE: 오타수정
    log.debug("채널 삭제 요청 - 삭제된 채널아디이디: {}", channelId);
  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(readStatus -> readStatus.getUser().getId())
          .forEach(participantIds::add);
    }

    return channelMapper.toDto(channel, participantIds, lastMessageAt);
  }
}
