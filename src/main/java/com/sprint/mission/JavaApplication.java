package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class JavaApplication {
    public static void main(String[] args) {

        // ===== User =====
        UserService userService = new JCFUserService();

        // 1. 등록
        User user = userService.create("sungjun", "sungjun@example.com");
        System.out.println("등록: " + user.getUserName());

        // 2. 단건 조회
        User foundUser = userService.findById(user.getId());
        System.out.println("단건 조회: " + foundUser.getUserName());

        // 3. 전체 조회
        System.out.println("전체 사용자 수: " + userService.findAll().size());

        // 4. 수정
        userService.update(user.getId(), "new-sungjun", "new@example.com");
        User updatedUser = userService.findById(user.getId());
        System.out.println("수정 결과: " + updatedUser.getUserName());

        // ===== Channel & Message =====
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        // 1. 등록
        Channel channel = channelService.create("general", "일반 대화 채널");
        Message message = messageService.create("첫 번째 메시지", user.getId(), channel.getId());
        System.out.println("생성 시간: " + message.getCreatedAt());
        System.out.println("수정 시간: " + message.getUpdatedAt());

        // 2. 메시지 조회 및 메시지 전체 조회
        System.out.println(messageService.findById(message.getId()).getContent());
        System.out.println("메시지 수: " + messageService.findAll().size());

        // 3. 수정
        messageService.update(message.getId(), "수정된 메시지");
        Message updated = messageService.findById(message.getId());
        System.out.println("생성 시간: " + updated.getCreatedAt());
        System.out.println("수정 시간: " + updated.getUpdatedAt());
        System.out.println(updated.getContent());

        // 4. 삭제
        messageService.delete(message.getId());
        System.out.println("삭제 후 메시지 수: " + messageService.findAll().size());
    }

}