package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChatRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChatService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class FileChatService implements ChatService {
    private final FileChatRepository filechatRepository = new FileChatRepository();
    private final FileUserRepository fileuserRepository = new FileUserRepository();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public Message messageCreate(String author, String message) {
        User user = fileuserRepository.findByUser(author)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + author));

        Message newMessage = new Message(user, message);
        filechatRepository.messageAdd(newMessage);
        filechatRepository.chatFlush();

        return newMessage;
    }

    @Override
    public void messageUpdate(UUID messageId, String updateMessageName) {
        Message message = filechatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지가 없습니다: " + messageId));

        message.updateMessage(updateMessageName);
        filechatRepository.chatFlush();
        System.out.println("메시지를 "+updateMessageName+"로 수정했습니다.");
    }

    @Override
    public void messageDelete(UUID messageId) {
        Message messages = filechatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + messageId));

        filechatRepository.delete(messages);
        filechatRepository.chatFlush();
        System.out.println(messageId+" 해당 메시지를 삭제했습니다.");
    }

    @Override
    public List<Message> allPrintMessage() {
        return filechatRepository.findAllMessage();
    }

    @Override
    public void printMessage(UUID messageId) {
        Message message = filechatRepository.findByMessage(messageId)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 메시지가 없습니다: " + messageId));

        System.out.printf("메시지 ID: %s, 메시지: %s \n", message.getMessageId(),message.getMessage());
    }
}
