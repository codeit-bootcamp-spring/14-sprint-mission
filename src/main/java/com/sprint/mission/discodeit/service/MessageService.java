package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Message;

public interface MessageService extends Service<Message> {

    void update(Message message, String newtext);


}
