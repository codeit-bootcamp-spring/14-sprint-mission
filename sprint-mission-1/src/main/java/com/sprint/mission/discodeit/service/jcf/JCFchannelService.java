package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFchannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Scanner;

public class JCFchannelService implements ChannelService {
    private final JCFchannelRepository jcFchannelRepository = new JCFchannelRepository();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public Channel channelCreate(String channelName) {
        if (jcFchannelRepository.findByChannel(channelName).isPresent()){
            throw new IllegalArgumentException("이미 생성된 채널의 이름입니다: "+channelName);
        }

        System.out.println("채널 생성이 완료되었습니다: "+channelName);
        Channel channel = new Channel(channelName);

        return jcFchannelRepository.channelAdd(channel);
    }

    @Override
    public void channelUpdate(String channelName, String updateChannelName) {
        Channel channel = jcFchannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다: " + channelName));


        channel.updateName(updateChannelName);
        System.out.println(channelName+" 채널의 이름을 "+updateChannelName+"로 수정했습니다.");
    }

    @Override
    public void channelDelete(String channelName) {
        Channel channel = jcFchannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다: " + channelName));

        System.out.println(channelName+" 채널을 삭제했습니다.");
        jcFchannelRepository.delete(channel);
    }

    @Override
    public List<Channel> allPrintChannel() {
        return jcFchannelRepository.findAllChannel();
    }

    @Override
    public void printChannel(String channelName) {
        Channel channel = jcFchannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 채널이 없습니다: " + channelName));

        System.out.printf("채널 이름: %s, 채널 아이디: %s \n", channel.getChannelName(),channel.getChannelId());
    }
}
