package com.sprint.mission.discodeit.domain.service.channel;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel makeChannel(Channel channel) {

        return channelRepository.saveEntity(channel);
    }

    @Override
    public Channel findChannelById(UUID channelId) {

        return channelRepository.findById(channelId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.CHANNEL_NOT_FOUND));
    }

    @Override
    public Channel updateChannelName(UUID channelId, String updateName) {

        //채널 조회 검증
        Channel channel = this.findChannelById(channelId);

        channel.validUpdatable();
        channel.updateChannelName(updateName);

        return channelRepository.saveEntity(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {

        Channel channel = this.findChannelById(channelId);
        channelRepository.deleteEntity(channel.getId());
    }

    @Override
    public List<Channel> findAllChannel() {
        return channelRepository.findAllEntity();
    }

    @Override
    public List<Channel> findAllChannelByIds(List<UUID> channelIdList) {
        return channelRepository.findAllChannelByIds(channelIdList);
    }

    @Override
    public List<Channel> findAllPublicChannel() {
        return channelRepository.findAllPublicChannel();
    }
}
