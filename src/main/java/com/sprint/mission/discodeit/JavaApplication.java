package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();

        User user1 = userService.create("psh", "psh@example.com");
        System.out.println("등록됨(User): " + user1.getName() + " / " + user1.getId());

        Channel channel1 = channelService.create("자유게시판");
        System.out.println("등록됨(Channel): " + channel1.getChannelName() + " / " + channel1.getId());

        Message message1 = messageService.create("안녕하세요!", channel1.getId(), user1.getId());
        System.out.println("등록됨(Message): " + message1.getContent() + " / " + message1.getId());

        User foundUser = userService.find(user1.getId());
        System.out.println("단건 조회(User): " + foundUser.getName());

        userService.create("psh2", "psh2@example.com");
        List<User> allUsers = userService.findAll();
        System.out.println("전체 조회(User), 총 인원: " + allUsers.size());
        for (User u : allUsers) {
            System.out.println(" - " + u.getName());
        }

        userService.update(user1.getId(), "psh-new", "psh-new@example.com");
        User updatedUser = userService.find(user1.getId());
        System.out.println("수정 후 조회(User): " + updatedUser.getName() + " / " + updatedUser.getEmail());

        userService.delete(user1.getId());
        User deletedUser = userService.find(user1.getId());
        System.out.println("삭제 후 조회(User) 결과: " + deletedUser);

        channelService.update(channel1.getId(), "공지사항");
        Channel updatedChannel = channelService.find(channel1.getId());
        System.out.println("수정 후 조회(Channel): " + updatedChannel.getChannelName());

        messageService.update(message1.getId(), "안녕하세요! (수정됨)");
        Message updatedMessage = messageService.find(message1.getId());
        System.out.println("수정 후 조회(Message): " + updatedMessage.getContent());

        messageService.delete(message1.getId());
        channelService.delete(channel1.getId());
        System.out.println("삭제 후 조회(Message) 결과: " + messageService.find(message1.getId()));
        System.out.println("삭제 후 조회(Channel) 결과: " + channelService.find(channel1.getId()));
    }
}