package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChatService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileChatService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;
import java.util.UUID;

public class FileController{
    private final ChannelService channelService= new FileChannelService();
    private final UserService userService = new FileUserService();
    private final ChatService chatService = new FileChatService();

    public void fileController(){

        //채널
        // 채널 리스트 전체 출력
        List<Channel> channels = channelService.allPrintChannel();
        for (Channel channel:channels){
            System.out.println(channel.getChannelName());
        }
        // 채널 수정
        try {
            channelService.channelUpdate("11번 채널", "업데이트 11번 채널");
        }catch (NameExistsException e){
            System.out.println("채널을 업데이트 할 수 없습니다.");
            System.out.println(e.getMessage());
        }
        channelService.channelCreate("1번 채널");
        // 채널 삭제
        channelService.channelDelete("1번 채널");
        // 채널 전체 출력
        for (Channel channel:channelService.allPrintChannel()){
            System.out.printf("채널 ID: %s, 채널 이름: %s\n", channel.getChannelId(), channel.getChannelName());
        }


        //유저
        List<User> users = userService.allPrintUser();
        for (User user:users){
            System.out.println(user.getUserName());
        }

        userService.userCreate("2번 채널", "고신재");

        try {
            userService.userUpdate("김예준", "개명한 김예준");
        }catch (NameExistsException e){
            System.out.println("유저를 업데이트 할 수 없습니다.");
            System.out.println(e.getMessage());
        }

        userService.userDelete("고신재");

        for (User user:userService.allPrintUser()){
            System.out.printf("유저 ID: %s, 유저 이름: %s, 채널: %s\n", user.getUserId(), user.getUserName(), user.getChannel().getChannelName());
        }

        //메시지
        List<Message> messages = chatService.allPrintMessage();
        for (Message message:messages){
            System.out.println(message.getAuthor().getUserName()+": "+message.getMessage());
        }

        chatService.messageCreate("김예준", "오늘도 좋은 하루3333!");

        try {
            chatService.messageUpdate(UUID.fromString("cc5885af-b96c-4949-923b-e25a43be473d"), "이것은 수정된 메시지입니다.");
        }catch (NameExistsException e){
            System.out.println("유저를 업데이트 할 수 없습니다.");
            System.out.println(e.getMessage());
        }

//        chatService.messageDelete("홍길동");

        for (Message message:chatService.allPrintMessage()){
            System.out.printf("메시지 ID: %s, 유저 이름: %s(%s)\n 메시지: %s\n", message.getMessageId(), message.getAuthor().getUserName(), message.getAuthor().getChannel().getChannelName(), message.getMessage());
        }
    }
}
