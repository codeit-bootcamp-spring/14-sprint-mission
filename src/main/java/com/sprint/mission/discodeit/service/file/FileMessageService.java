package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FileMessageService extends FileService<Message> implements MessageService {
    public FileMessageService(){
        super("messages.ser");
    }

    @Override
    public void update(Message message, String newtext) {
        Map<UUID, Message> data = loadData();
        Message existing = data.get(message.getId());
        if(Objects.isNull(existing)){
            throw new RuntimeException("존재하지 않는 유저 입니다");
        }
        existing.setText(newtext);
        saveData(data);
    }
}
