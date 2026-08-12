package com.sprint.mission.discodeit.service.domain.channel;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Qualifier("channelService")
public class ChannelDomainServiceImpl implements ChannelDomainService {

    private final ChannelRepository channelRepository;

    public ChannelDomainServiceImpl(
            ChannelRepository channelRepository
    ) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel channel) {
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        if (Objects.isNull(channelId)) {
            throw new CustomException(ExceptionType.CHANNEL_ID_IS_NULL);
        }

        return channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND, channelId));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(Channel updatingChannel) {
        Channel originalChannel = findById(updatingChannel.getId());

        if (originalChannel.getChannelType() == ChannelType.PRIVATE) {
            throw new CustomException(
                    ExceptionType.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED,
                    updatingChannel.getId()
            );
        }

        return channelRepository.save(updatingChannel);
    }

    @Override
    public void delete(UUID channelId) {
        findById(channelId);    // 없으면 CHANNEL_NOT_FOUND
        channelRepository.delete(channelId);
    }
}
