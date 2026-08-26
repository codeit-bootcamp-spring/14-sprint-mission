package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file")
public class FileChannelRepository extends FileRepository<Channel> implements ChannelRepository {


    public FileChannelRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        super(fileDirectory, "channel");
    }

    @Override
    public List<Channel> findAllByType(ChannelType type) {
        return findAll().stream()
            .filter(channel -> channel.getType().equals(type))
            .toList();
    }


}
