package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.application.dto.ChannelRequest;
import com.sprint.mission.discodeit.channel.application.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelCommand;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelModifier;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRegister;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRemover;
import com.sprint.mission.discodeit.channel.application.provided.command.CreateChannel;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelFinder;
import com.sprint.mission.discodeit.channel.application.required.RoleChecker;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRemover;
import com.sprint.mission.discodeit.message.application.provided.query.MessageTimeFinder;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.readstatus.application.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.Role;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelAppService implements ChannelRegister, ChannelModifier, ChannelRemover,
    ChannelFinder {

  private final ChannelMapper channelMapper;
  private final ChannelCommand channelCommand;
  private final ChannelEntityFinder channelEntityFinder;
  private final MessageTimeFinder messageTimeFinder;
  private final MessageRemover messageRemover;
  private final UserEntityFinder userEntityFinder;
  private final ReadStatusCommand readStatusCommand;
  private final ReadStatusEntityFinder readStatusEntityFinder;
  private final RoleChecker roleChecker;


  @Override
  public ChannelDto modify(UUID channelId, ChannelRequest request) {

    Channel channel = channelEntityFinder.getEntityById(channelId);
    authorizeAuthority(channel.getType());
    Channel updated = channelCommand.update(channel, request.newName(), request.newDescription());
    Instant lastMessageAt = messageTimeFinder.getLastMessageAt(channelId).orElse(null);
    return channelMapper.toDto(updated, List.of(), lastMessageAt);
  }

//  @Override
//  public ChannelDto registerPublic(PublicChannelCreateRequest request) {
//    Channel channel = channelCommand.createPublic(request.name(), request.description());
//    log.info("공개 채널 생성: channel = {}", channel);
//    return channelMapper.toDto(channel, List.of(), null);
//  }

//  @Override
//  public ChannelDto registerPrivate(PrivateChannelCreateRequest request) {
//    Channel channel = channelCommand.createPrivate();
//    List<User> participants = userEntityFinder.getEntitiesById(request.participantIds());
//    readStatusCommand.createAll(channel, participants, Instant.now());
//    log.info("비공개 채널 생성: channel = {}", channel);
//    return channelMapper.toDto(channel, participants, null);
//  }

  @Override
  public ChannelDto register(CreateChannel command) {

    return switch (command) {
      case CreateChannel.Public(var name, var description) -> {
        authorizeAuthority(ChannelType.PUBLIC);
        Channel aPublic = channelCommand.createPublic(name, description);
        yield channelMapper.toDto(aPublic, List.of(), null);
      }
      case CreateChannel.Private(var participantsIds) -> {
        Channel aPrivate = channelCommand.createPrivate();
        List<User> participants = userEntityFinder.getEntitiesById(participantsIds);
        readStatusCommand.createAll(aPrivate, participants, Instant.now());
        yield channelMapper.toDto(aPrivate, participants, null);
      }
    };
  }


  @Override
  public void delete(UUID channelId) {
    Channel channel = channelEntityFinder.getEntityById(channelId);
    authorizeAuthority(channel.getType());
    readStatusCommand.deleteAllByChannelId(channelId);
    messageRemover.removeAllByChannelId(channelId);
    channelCommand.delete(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> getAllByUserId(UUID userId) {
    List<ChannelDto> list = new ArrayList<>();
    List<Channel> channels = channelEntityFinder.getAllByUserId(userId);
    for (Channel channel : channels) {
      Instant instant = messageTimeFinder.getLastMessageAt(channel.getId()).orElse(null);
      if (channel.getType() == ChannelType.PUBLIC) {
        ChannelDto channelDto = channelMapper.toDto(channel, List.of(), instant);
        list.add(channelDto);
      }
      if (channel.getType() == ChannelType.PRIVATE) {
        List<UUID> participantsId = readStatusEntityFinder.getEntitiesByChannelId(channel.getId())
            .stream().map(ReadStatus::getUser).map(User::getId).toList();
        List<User> participants = userEntityFinder.getEntitiesById(participantsId);
        ChannelDto channelDto = channelMapper.toDto(channel, participants, instant);
        list.add(channelDto);
      }
    }
    return list;
  }

  private void authorizeAuthority(ChannelType type) {
    if (type == ChannelType.PUBLIC) {
      roleChecker.checkRole(Role.CHANNEL_MANAGER);
    }
  }
}
