package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
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
    public ChannelResponse createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = Channel.createPrivate();
        Channel savedChannel = channelRepository.save(channel);
        List<UUID> uuids = privateChannelCreateRequest.participantIds();
        for (UUID userId : uuids) {
            ReadStatus readStatus = ReadStatus.create(userId, savedChannel.getId());
            readStatusRepository.save(readStatus);
        }
        return ChannelResponse.from(channel);
    }

    @Override
    public ChannelResponse createPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = Channel.createPublic(publicChannelCreateRequest);
        Channel savedChannel = channelRepository.save(channel);
        return ChannelResponse.from(channel);
    }

    @Override
    public ChannelDto findChannel(UUID uuid) {
        Channel channel = channelRepository.findChannel(uuid).orElseThrow(
            () -> new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND)
        );
        Instant recentMessageAt = messageRepository.findByChannelId(uuid).stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo).orElse(null);
        if (channel.getType() == ChannelType.PRIVATE) {
            List<UUID> privateUserIds = readStatusRepository.findByChannel(uuid).stream()
                .map(ReadStatus::getUserId).toList();
            return ChannelDto.from(channel, recentMessageAt, privateUserIds);
        }

        return ChannelDto.from(channel, recentMessageAt, null);
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest channelRequest) {
        Channel channel = channelRepository.findChannel(id).orElseThrow(
            () -> new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND)
        );
        Channel updatedChannel = channel.update(channelRequest);
        channelRepository.save(updatedChannel);
        return ChannelResponse.from(updatedChannel);
    }

    @Override
    public void delete(UUID uuid) {
        channelRepository.delete(uuid);
        readStatusRepository.deleteByChannelId(uuid);
        messageRepository.deleteByChannelId(uuid);
    }

    @Override
    public List<ChannelDto> findByUserId(UUID userId) {
        List<ReadStatus> readStatusList = readStatusRepository.findByUser(userId);
        List<UUID> uuidList = readStatusList.stream().map(ReadStatus::getChannelId).toList();
        List<ChannelDto> channelList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            ChannelDto channel = findChannel(uuid);
            channelList.add(channel);
        }
        return channelList;
    }
}
