package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    private static FileChannelRepository instance;

    public FileChannelRepository() {
        super(FILE_PATH);
    }

    public static FileChannelRepository getInstance() {
        if (instance == null){
            instance = new FileChannelRepository();
        }
        return instance;
    }
}
