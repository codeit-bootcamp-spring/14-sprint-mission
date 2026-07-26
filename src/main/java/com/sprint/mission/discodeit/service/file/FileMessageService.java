package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    FileMessageRepository fileMessageRepository;

    public FileMessageService(FileMessageRepository fileMessageRepository){
        this.fileMessageRepository = fileMessageRepository;
    }

    @Override
    public Message create(String message, UUID ChannelId, UUID userId) {
        Message message1 = new Message(message, ChannelId, userId);
        fileMessageRepository.save(message1);

        return message1;
    }


    @Override
    public Message read(UUID id) {
        return null;
    }

    @Override
    public void update(Message t) {

    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<Message> readAll() {
        return List.of();
    }
}
