package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.controller.ConsoleController;
import com.sprint.mission.discodeit.controller.FileController;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ChatRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChatRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChatRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChatService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChatService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplicationMain {
    public static void main(String[] args) {
        // JCF 환경
//        UserRepository userRepository = new JCFUserRepository();
//        ChannelRepository channelRepository = new JCFChannelRepository();
//        ChatRepository chatRepository = new JCFChatRepository();

         // File 환경
         UserRepository userRepository = new FileUserRepository();
         ChannelRepository channelRepository = new FileChannelRepository();
         ChatRepository chatRepository = new FileChatRepository();

        ChannelService channelService = new BasicChannelService(channelRepository);
        UserService userService = new BasicUserService(userRepository, channelRepository);
        ChatService chatService = new BasicChatService(chatRepository, userRepository);

        // JCF 테스트
//        Channel channel = channelService.channelCreate("1번채널");
//        User user = userService.userCreate("1번채널", "공감진");
//        Message message = chatService.messageCreate("공감진", "HIHI");
//        System.out.println("생성된 유저: " + user.getUserName()+", 채널: "+user.getChannel().getChannelName());
//        System.out.printf("유저: %s(%s)\n메시지: %s", message.getAuthor().getUserName(), message.getAuthor().getChannel().getChannelName(), message.getMessage());

        // File 테스트
        for (Message message:chatService.allPrintMessage()){
            System.out.printf("유저: %s(%s)\n메시지: %s\n", message.getAuthor().getUserName(), message.getAuthor().getChannel().getChannelName(), message.getMessage());
        }

//        ConsoleController consoleController = new ConsoleController();
//
//        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
//        consoleController.consoleContorller();

//        FileController fileController = new FileController();
//        fileController.fileController();
    }
}