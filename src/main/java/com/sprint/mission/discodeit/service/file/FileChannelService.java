package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FileChannelService extends FileService<Channel> implements ChannelService {

    public FileChannelService(){
        super("channels.ser");
    }

    @Override
    public void update(Channel channel, String newname) {
        Map<UUID, Channel> data = loadData();
        Channel existing = data.get(channel.getId());
        if(Objects.isNull(existing)){
            throw new RuntimeException("존재하지 않는 유저 입니다");
        }
        existing.setName(newname);
        saveData(data);

    }
}
