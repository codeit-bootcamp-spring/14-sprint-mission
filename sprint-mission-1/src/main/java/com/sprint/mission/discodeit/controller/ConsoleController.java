package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChatService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFchannelService;
import com.sprint.mission.discodeit.service.jcf.JCFchatService;
import com.sprint.mission.discodeit.service.jcf.JCFuserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleController {
    public static final Scanner SC = new Scanner(System.in);
    ChannelService channelService = new JCFchannelService();
    UserService userService = new JCFuserService();
    ChatService chatService = new JCFchatService();

    public void consoleContorller(){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("""
                --------------------
                 1. 채널 관리
                 2. 유저 관리
                 3. 종료
                --------------------
                 숫자를 입력하세요:\s""");

        String input = SC.next();

        switch (input){
            case "1" -> {
                while (true) {
                    System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
                    System.out.print("""
                            --------------------
                             1. 채널 생성
                             2. 채널 수정
                             3. 채널 삭제
                             4. 채널 단건 조회
                             5. 채널 리스트 조회
                             6. 종료
                            --------------------
                             숫자를 입력하세요:\s""");

                    String serverInput = SC.next();
                    switch (serverInput) {
                        case "1" -> {
                            System.out.print("생성할 채널의 이름을 입력하세요: ");
                            String channelName = SC.next();
                            channelService.channelCreate(channelName);
                        }
                        case "2" -> {
                            System.out.print("수정될 기존 채널의 이름을 입력하세요: ");
                            String name = SC.next();
                            System.out.print("수정할 채널의 이름을 입력하세요: ");
                            String newName = SC.next();
                            channelService.channelUpdate(name, newName);
                        }
                        case "3" -> {
                            System.out.print("삭제할 채널의 이름을 입력하세요: ");
                            String channelName = SC.next();
                            channelService.channelDelete(channelName);
                        }
                        case "4" -> {
                            System.out.print("조회할 채널의 이름을 입력하세요: ");
                            String channelName = SC.next();
                            channelService.printChannel(channelName);
                        }
                        case "5" -> {
                            List<Channel> channels = channelService.allPrintChannel();

                            if (channels.isEmpty()) {
                                System.out.println("등록된 채널이 없습니다.");
                                return;
                            }

                            System.out.println("--- 채널 목록 ---");
                            for (Channel channel : channels) {
                                System.out.printf("채널의 이름: %s 입니다.\n", channel.getChannelName());
                            }
                        }
                        case "6" -> {
                            System.exit(0);
                        }
                        default -> {
                            throw new IllegalArgumentException("잘못 된 숫자를 입력했습니다: "+input);
                        }
                    }
                }
            }

            case "2" -> {
                while (true) {
                    System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
                    System.out.print("""
                            --------------------
                             1. 유저 생성
                             2. 유저 수정
                             3. 유저 삭제
                             4. 유저 단건 조회
                             5. 유저 리스트 조회
                             6. 채팅방 입장
                             7. 종료
                            --------------------
                             숫자를 입력하세요:\s""");

                    String userInput = SC.next();
                    switch (userInput) {
                        case "1" -> {
                            System.out.print("생성할 유저의 이름을 입력하세요: ");
                            String userlName = SC.next();
                            userService.userCreate(userlName);
                        }
                        case "2" -> {
                            System.out.print("수정될 기존 유저의 이름을 입력하세요: ");
                            String name = SC.next();
                            System.out.print("수정할 유저의 이름을 입력하세요: ");
                            String newName = SC.next();
                            userService.userUpdate(name, newName);
                        }
                        case "3" -> {
                            System.out.print("삭제할 유저의 이름을 입력하세요: ");
                            String userName = SC.next();
                            userService.userDelete(userName);
                        }
                        case "4" -> {
                            System.out.print("조회할 유저의 이름을 입력하세요: ");
                            String userName = SC.next();
                            userService.printUser(userName);
                        }
                        case "5" -> {
                            List<User> users = userService.allPrintUser();

                            if (users.isEmpty()) {
                                System.out.println("등록된 유저가 없습니다.");
                                return;
                            }

                            System.out.println("--- 유저 목록 ---");
                            for (User user : users) {
                                System.out.printf("유저의 이름: %s 입니다.\n", user.getUserName());
                            }
                        }
                        case "6" -> {

                            while (true) {
                                System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
                                System.out.print("""
                            --------------------
                             1. 메시지 작성
                             2. 메시지 수정
                             3. 메시지 삭제
                             4. 메시지 단건 조회
                             5. 메시지 리스트 조회
                             6. 종료
                            --------------------
                             숫자를 입력하세요:\s""");

                                String messageInput = SC.next();
                                switch (messageInput) {
                                    case "1" -> {
                                        System.out.print("메시지 작성 할 유저를 입력하세요: ");
                                        String author = SC.next();
                                        System.out.print("메시지를 입력하세요: ");
                                        String message = SC.next();
                                        chatService.messageCreate(author, message);
                                    }
                                    case "2" -> {
                                        System.out.print("수정될 메시지의 UUID를 입력하세요: ");
                                        String uuid = SC.next();
                                        System.out.print("수정할 메시지를 입력하세요: ");
                                        String message = SC.next();
                                        UUID messageId = UUID.fromString(uuid);
                                        chatService.messageUpdate(messageId, message);
                                    }
                                    case "3" -> {
                                        System.out.print("삭제할 메시지 유저의 UUID를 입력하세요: ");
                                        String uuid = SC.next();
                                        UUID messageId = UUID.fromString(uuid);
                                        chatService.messageDelete(messageId);
                                    }
                                    case "4" -> {
                                        System.out.print("조회할 메시지 유저의 UUID를 입력하세요: ");
                                        String uuid = SC.next();
                                        UUID messageId = UUID.fromString(uuid);
                                        chatService.printMessage(messageId);
                                    }
                                    case "5" -> {
                                        List<Message> messages = chatService.allPrintMessage();

                                        if (messages.isEmpty()) {
                                            System.out.println("등록된 메시지가 없습니다.");
                                            return;
                                        }

                                        System.out.println("--- 채팅 ---");
                                        for (Message message : messages) {
                                            System.out.printf("유저: %s, 메시지 ID: %s\n 메시지: %s\n", message.getAuthor().getUserName(), message.getMessageId(), message.getMessage());
                                        }
                                    }
                                    case "6" -> {
                                        System.exit(0);
                                    }
                                    default -> {
                                        throw new IllegalArgumentException("잘못 된 숫자를 입력했습니다: "+input);
                                    }
                                }
                            }
                        }
                        case "7" -> {
                            System.exit(0);
                        }
                        default -> {
                            throw new IllegalArgumentException("잘못 된 숫자를 입력했습니다: "+input);
                        }
                    }
                }
            }
            case "3" -> {
                System.exit(0);
            }
            default -> {
                throw new IllegalArgumentException("잘못 된 숫자를 입력했습니다: "+input);
            }
        }
    }
}
