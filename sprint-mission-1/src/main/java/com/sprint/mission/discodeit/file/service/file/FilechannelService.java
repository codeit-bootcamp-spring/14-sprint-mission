package com.sprint.mission.discodeit.file.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.file.repository.file.FilechannelRepository;
import com.sprint.mission.discodeit.file.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class FilechannelService implements ChannelService {
    private final FilechannelRepository filechannelRepository = new FilechannelRepository();

    @Override
    public void channelInit() {
        filechannelRepository.channelLoad();
    }

    @Override
    public void channelCreate() {
        List<String> channelName = filechannelRepository.readChannelName();
        List<Channel> newChannels = new ArrayList<>();

        for (String name : channelName) {
            if (filechannelRepository.findByChannel(name).isEmpty()) {
                System.out.println("채널 생성이 완료되었습니다: " + name);
                newChannels.add(new Channel(name));
            }
        }
        filechannelRepository.channelAdd(newChannels);
    }

    @Override
    public void channelUpdate(String channelName, String updateChannelName) {
        if (filechannelRepository.findByChannel(updateChannelName).isPresent()){
            throw NameExistsException.ofChannel(updateChannelName);
        }

        Channel channel = filechannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다: " + channelName));

        channel.updateName(updateChannelName);
        System.out.println(channelName+" 채널의 이름을 "+updateChannelName+"로 수정했습니다.");

        filechannelRepository.channelFlush();
    }

    @Override
    public void channelDelete(String channelName) {
        Channel channel = filechannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다: " + channelName));

        System.out.println(channelName+" 채널을 삭제했습니다.");
        filechannelRepository.delete(channel);

        filechannelRepository.channelFlush();
    }

    @Override
    public List<Channel> allPrintChannel() {
        return filechannelRepository.findAllChannel();
    }

    @Override
    public void printChannel(String channelName) {
        Channel channel = filechannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 채널이 없습니다: " + channelName));

        System.out.printf("채널 이름: %s, 채널 아이디: %s \n", channel.getChannelName(),channel.getChannelId());
    }
}
