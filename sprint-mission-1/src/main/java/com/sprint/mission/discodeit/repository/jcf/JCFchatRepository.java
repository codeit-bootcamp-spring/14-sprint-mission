package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.chatRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class JCFchatRepository implements chatRepository {
    protected final List<Message> messages = new ArrayList<>();

    @Override
    public Message chatCreate(User user, String message) {
        messages.add(new Message(user, message));
        return null;
    }
}
