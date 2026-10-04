package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.application.dto.ChannelRequest;
import com.sprint.mission.discodeit.channel.application.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.application.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.application.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelCommand;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelModifier;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRegister;
import com.sprint.mission.discodeit.channel.application.provided.command.ChannelRemover;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelFinder;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.message.application.provided.query.MessageTimeFinder;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.readstatus.application.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelAppService implements ChannelRegister, ChannelModifier, ChannelRemover,
    ChannelFinder {

  private final ChannelMapper channelMapper;
  private final ChannelCommand channelCommand;
  private final ChannelEntityFinder channelEntityFinder;
  private final MessageTimeFinder messageTimeFinder;
  private final UserEntityFinder userEntityFinder;
  private final ReadStatusCommand readStatusCommand;
  private final ReadStatusEntityFinder readStatusEntityFinder;


  @Override
  public ChannelDto modify(UUID channelId, ChannelRequest request) {

    Channel channel = channelEntityFinder.getEntityById(channelId);
    Channel updated = channelCommand.update(channel, request.newName(), request.newDescription());
    Instant lastMessageAt = messageTimeFinder.getLastMessageAt(channelId).orElse(null);
    return channelMapper.toDto(updated, List.of(), lastMessageAt);
  }

  @Override
  public ChannelDto registerPublic(PublicChannelCreateRequest request) {
    Channel channel = channelCommand.createPublic(request.name(), request.description());
    return channelMapper.toDto(channel, List.of(), null);
  }

  @Override
  public ChannelDto registerPrivate(PrivateChannelCreateRequest request) {
    Channel channel = channelCommand.createPrivate();
    List<User> participants = userEntityFinder.getEntitiesById(request.participantIds());
    readStatusCommand.createAll(channel, participants, Instant.now());
    return channelMapper.toDto(channel, participants, null);
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelEntityFinder.getEntityById(channelId);

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
}
