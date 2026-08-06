package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel channelCreate(String channelName) {
        if (channelRepository.findByChannel(channelName).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channelName);
        }

        Channel channel = new Channel(channelName);
        return channelRepository.channelAdd(channel);
    }

    @Override
    public void channelUpdate(String channelName, String updateChannelName) {
        if (channelRepository.findByChannel(updateChannelName).isPresent()){
            throw NameExistsException.ofChannel(updateChannelName);
        }

        Channel channel = channelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다: " + channelName));

        channel.updateName(updateChannelName);
        System.out.println(channelName+" 채널의 이름을 "+updateChannelName+"로 수정했습니다.");
        channelRepository.channelAdd(channel);
    }

    @Override
    public void channelDelete(String channelName) {
        Channel channel = channelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다: " + channelName));

        System.out.println(channelName+" 채널을 삭제했습니다.");
        channelRepository.delete(channel);
    }

    @Override
    public List<Channel> allPrintChannel() {
        return channelRepository.findAllChannel();
    }

    @Override
    public Channel printChannel(String channelName) {
        Channel channel = channelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 채널이 없습니다: " + channelName));

        System.out.printf("채널 이름: %s, 채널 아이디: %s \n", channel.getChannelName(),channel.getChannelId());

        return channel;
    }
}
