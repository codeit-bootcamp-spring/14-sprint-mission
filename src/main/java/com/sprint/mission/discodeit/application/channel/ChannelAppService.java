package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.application.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.application.channel.dto.ChannelRequest;
import com.sprint.mission.discodeit.application.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.application.channel.provided.command.ChannelCommand;
import com.sprint.mission.discodeit.application.channel.provided.command.ChannelModifier;
import com.sprint.mission.discodeit.application.channel.provided.command.ChannelRegister;
import com.sprint.mission.discodeit.application.channel.provided.command.ChannelRemover;
import com.sprint.mission.discodeit.application.channel.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.application.channel.provided.query.ChannelFinder;
import com.sprint.mission.discodeit.application.message.provided.query.MessageTimeFinder;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.application.readstatus.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
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
