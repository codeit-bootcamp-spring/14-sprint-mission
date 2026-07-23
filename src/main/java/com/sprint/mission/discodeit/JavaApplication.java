package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.entity.dto.user.UserDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository("src/main/java/com/sprint/mission/discodeit/repository/file/user.ser");
        MessageRepository messageRepository = new JCFMessageRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();

        UserService userService = new JCFUserService(userRepository, messageRepository, channelRepository);
        MessageService messageService = new JCFMessageService(messageRepository, channelRepository);
        ChannelService channelService = new JCFChannelService(channelRepository, messageRepository);

        User u1 = userService.createAccount(new UserDto("soomin"));
        User u2 = userService.createAccount(new UserDto("sumin"));
        User u3 = userService.createAccount(new UserDto("ssoomin"));
        User u4 = userService.createAccount(new UserDto("ssumin"));
        userService.updateUser(u1.getId(), new UserDto("UpdatedSoomin"));
        System.out.println(userService.getAllUsers());

        Channel c1 = channelService.createChannel(new ChannelCreationDto("channel1", List.of(u1.getId(), u2.getId())));
        Channel c2 = channelService.createChannel(new ChannelCreationDto("channel2", List.of(u3.getId(), u4.getId())));
        System.out.println(channelService.getAllChannels());

        Message m1 = messageService.createMessage(new MessageCreationDto("ssoomin1", u1.getId(), c1.getId()));
//        messageService.createMessage(new MessageCreationDto("ssoomin1", u2.getId(), c2.getId())); // 채널에 없는 유저가 메시지 보낼 시 오류
        System.out.println(messageService.getAllMessages());

    }
}
