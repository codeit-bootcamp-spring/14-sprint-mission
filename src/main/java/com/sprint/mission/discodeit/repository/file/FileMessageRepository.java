package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private static final String FILE_PATH = "messages.ser";

    private static FileMessageRepository instance;

    public FileMessageRepository() {
        super(FILE_PATH);
    }

    public static FileMessageRepository getInstance() {
        if (instance == null){
            instance = new FileMessageRepository();
        }
        return instance;
    }
}
