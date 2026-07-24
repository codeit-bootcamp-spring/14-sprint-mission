package com.sprint.mission.discodeit.file.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    void chatInit();
    void messageCreate(String author, String message);
    void messageUpdate(UUID messageId, String updateMessageName);
    void messageDelete(UUID messageId);
    List<Message> allPrintMessage();
    void printMessage(UUID messageId);
}
