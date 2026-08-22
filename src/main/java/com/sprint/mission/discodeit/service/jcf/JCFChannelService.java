package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponse createPrivate(PrivateChannelCreate privateChannelCreate) {
        Channel channel = Channel.createPrivate();
        Channel savedChannel = channelRepository.save(channel);
        List<UUID> uuids = privateChannelCreate.userIds();
        for (UUID userId : uuids) {
            ReadStatus readStatus = ReadStatus.create(userId,savedChannel.getId());
            readStatusRepository.save(readStatus);
        }
        return findChannel(savedChannel.getId());
    }

    @Override
    public ChannelResponse createPublic(PublicChannelCreate publicChannelCreate) {
        Channel channel = Channel.createPublic(publicChannelCreate);
        Channel savedChannel = channelRepository.save(channel);
        return findChannel(savedChannel.getId());
    }

    @Override
    public ChannelResponse findChannel(UUID uuid) {
        Channel channel = channelRepository.findChannel(uuid).orElseThrow(
            () -> new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND)
        );
        Long recentMessageAt = messageRepository.findByChannelId(uuid).stream()
            .map(Message::getCreatedAt)
            .max(Long::compareTo).orElse(null);
        if (channel.getType() == ChannelType.PRIVATE) {
            List<UUID> privateUserIds = readStatusRepository.findByChannel(uuid).stream()
                .map(ReadStatus::getUserId).toList();
            return ChannelResponse.from(channel, recentMessageAt, privateUserIds);
        }

        return ChannelResponse.from(channel, recentMessageAt, null);
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest channelRequest) {
        Channel channel = channelRepository.findChannel(id).orElseThrow(
            () -> new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND)
        );
        Channel updateChannel = channel.update(channelRequest);
        channelRepository.save(updateChannel);
        return findChannel(updateChannel.getId());
    }

    @Override
    public void delete(UUID uuid) {
        channelRepository.delete(uuid);
        readStatusRepository.deleteByChannelId(uuid);
        messageRepository.deleteByChannelId(uuid);
    }

    @Override
    public List<ChannelResponse> findByUserId(UUID userId) {
        List<ReadStatus> readStatusList =readStatusRepository.findByUser(userId);
        List<UUID> uuidList = readStatusList.stream().map(ReadStatus::getChannelId).toList();
        List<ChannelResponse> channelList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            ChannelResponse channel = findChannel(uuid);
            channelList.add(channel);
        }
        return channelList;
    }
}
