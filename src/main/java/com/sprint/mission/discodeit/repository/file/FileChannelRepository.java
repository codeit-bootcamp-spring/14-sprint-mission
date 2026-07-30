package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {
    final List<Channel> channelList;

    public FileChannelRepository() {
        this.channelList = new ArrayList<>();
    }


    @Override
    //
    public void save(Channel channel) {
        channelList.add(channel);
        //직렬화
        saveChannel();
    }

    // 채널 하나하나 순회하며 아이디 조회해보며 채널 가져오기
    @Override
    public Channel findById(UUID id) {
        for (Channel each : channelList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return channelList;
    }

    @Override
    public void delete(UUID id) {
        Channel target = findById(id);
        channelList.remove(target);
        saveChannel();
    }

    private void saveChannel() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("newchannellist.ser"))) {
            objectOutputStream.writeObject(channelList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
