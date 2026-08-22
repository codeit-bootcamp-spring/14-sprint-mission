package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
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
        return ChannelResponse.from(savedChannel, null, privateChannelCreate.userIds());
    }

    @Override
    public ChannelResponse createPublic(PublicChannelCreate publicChannelCreate) {
        Channel channel = Channel.createPublic(publicChannelCreate);
        Channel savedChannel = channelRepository.save(channel);
        return ChannelResponse.from(savedChannel, null, null);
    }

    @Override
    public ChannelResponse findChannel(UUID uuid) {
        Channel channel = channelRepository.findChannel(uuid).orElseThrow(
            () -> new IllegalArgumentException("해당 채널은 존재하지 않습니다.")
        );
        Long recentMessageAt = messageRepository.findByChannelId(uuid).stream()
            .map(message -> message.getCreatedAt())
            .max(Long::compareTo).orElse(null);
        if (channel.getType() == ChannelType.PRIVATE) {
            List<UUID> privateUserIds = readStatusRepository.findByChannel(uuid).stream()
                .map(ReadStatus::getUserId).toList();
            return ChannelResponse.from(channel, recentMessageAt, privateUserIds);
        }

        return ChannelResponse.from(channel, recentMessageAt, null);
    }

    @Override
    public ChannelResponse update(ChannelRequest channelRequest) {
        Channel channel = channelRepository.findChannel(channelRequest.uuid()).orElseThrow(
            () -> new IllegalArgumentException("해당 채널은 존재하지 않습니다.")
        );
        Channel updateChannel = channel.update(channelRequest);
        channelRepository.save(updateChannel);
        return ChannelResponse.from(updateChannel, null, null);
    }

    @Override
    public void delete(UUID uuid) {
        channelRepository.delete(uuid);
        readStatusRepository.deleteByChannelId(uuid);
        messageRepository.deleteByChannelId(uuid);
    }

    @Override
    public List<ChannelResponse> findByUserId(UUID userId) {
        return null;
    }
}
