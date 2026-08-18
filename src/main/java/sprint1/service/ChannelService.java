package sprint1.service;

import sprint1.entity.Channel;
import sprint1.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelType type, String name, String description);

    Channel find(UUID ChannelId);

    List<Channel> findAll();

    Channel update(UUID channelId, String newName, String newDescription);

    void delete(UUID channelId);
}
