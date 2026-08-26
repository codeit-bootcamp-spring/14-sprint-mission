package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository extends FileRepository<Message> implements MessageRepository {


    public FileMessageRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        super(fileDirectory, "message");
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();
    }


}
