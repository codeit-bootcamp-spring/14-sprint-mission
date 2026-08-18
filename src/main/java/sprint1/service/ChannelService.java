package sprint1.service;

import com.example.demo.levelTest1.entity.Channel;
import com.example.demo.levelTest1.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelType type, String name, String description);

    Channel find(UUID ChannelId);

    List<Channel> findAll();

    Channel update(UUID channelId, ChannelType newChannel);

    void delete(UUID channelId);
}
