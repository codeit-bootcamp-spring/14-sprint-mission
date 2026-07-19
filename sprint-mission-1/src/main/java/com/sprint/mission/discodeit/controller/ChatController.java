package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.chatRepository;
import com.sprint.mission.discodeit.service.jcf.JCFchatService;

import static com.sprint.mission.discodeit.controller.ConsoleController.SC;
import static com.sprint.mission.discodeit.controller.UserContorller.userContorller;

public class ChatController {
    private static final chatRepository CHAT = new JCFchatService();

    public static void chatContorller(User user){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("--------------------\n" +
                " 1. 유저 다시 선택\n" +
                " 2. 메시지 입력\n" +
                "--------------------\n 숫자를 입력하세요: ");

        String input = SC.next();
        switch (input){
            case "1" -> {
                userContorller();
            }
            case "2" -> {
                SC.nextLine();
                System.out.println("---메시지를 입력하세요---");
                String message = SC.nextLine();
                CHAT.chatCreate(user, message);
                CHAT.chatHistory();
                chatContorller(user);
            }
            default -> {
                System.out.print("잘못 된 숫자를 입력하였습니다.\n다시 시도해주세요.\n");
                chatContorller(user);
            }
        }
    }
}
