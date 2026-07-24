package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChatRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChatService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JCFChatService implements ChatService {
    private final JCFChatRepository jcFchatRepository = new JCFChatRepository();
    private final JCFUserRepository jcFuserRepository = new JCFUserRepository();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public Message messageCreate(String author, String message) {
        User user = jcFuserRepository.findByUser(author)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + author));

        Message newMessage = new Message(user, message);
        return jcFchatRepository.messageAdd(newMessage);
    }

    @Override
    public void messageUpdate(UUID messageId, String updateMessageName) {
        Message message = jcFchatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지가 없습니다: " + messageId));

        message.updateMessage(updateMessageName);
        System.out.println("메시지를 "+updateMessageName+"로 수정했습니다.");
    }

    @Override
    public void messageDelete(UUID messageId) {
        Message messages = jcFchatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + messageId));

        System.out.println(messageId+" 해당 메시지를 삭제했습니다.");
        jcFchatRepository.delete(messages);
    }

    @Override
    public List<Message> allPrintMessage() {
        return jcFchatRepository.findAllMessage();
    }

    @Override
    public void printMessage(UUID messageId) {
        Message message = jcFchatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 메시지가 없습니다: " + messageId));

        System.out.printf("메시지 ID: %s, 메시지: %s \n", message.getMessageId(),message.getMessage());
    }
}
