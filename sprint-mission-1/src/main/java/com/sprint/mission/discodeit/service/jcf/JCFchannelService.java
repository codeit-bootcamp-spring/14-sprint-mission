package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.channelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFchannelRepository;

import java.util.Scanner;

public class JCFchannelService extends JCFchannelRepository implements channelRepository {
    Scanner sc = new Scanner(System.in);

    @Override
    public void allPrintChannel() {
        for (Channel channel:super.channels){
            System.out.printf("채널의 이름: %s 입니다.\n", channel.getChannelName());
        }
    }

    @Override
    public void printChannel() {
        System.out.print("자세히 보고자 하는 채널의 이름을 말해주세요: ");
        String channelName = sc.next();
        Channel channel = findByChannel(channelName);

        System.out.println(channel.toString());
    }

    @Override
    public Channel selectedChannel() {
        allPrintChannel();
        System.out.print("선택할 채널의 이름을 말해주세요: ");
        String channelName = sc.next();
        Channel channel = findByChannel(channelName);

        if (channel != null){
            return channel;
        }

        System.out.println("채널을 찾을 수 없습니다.");
        return null;
    }
}
