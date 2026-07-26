package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

//@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        /* ========================= User ========================= */
        System.out.println("===== User =====");
        // 등록
        System.out.println("----- create() -----");
        User aaron = userService.create(new User("Aaron", 20, "aaron@naver.com"));
        User baron = userService.create(new User("Baron", 30, "baron@naver.com"));
        User caron = userService.create(new User("Caron", 40, "caron@naver.com"));

        // 단건 조회
        System.out.println("----- findById() -----");
        System.out.println(userService.findById(aaron.getId()));
        System.out.println(userService.findById(caron.getId()));

        // 다건 조회
        System.out.println("----- findAll() -----");
        List<User> userList = userService.findAll();
        userList.forEach(System.out::println);

        // 수정
        System.out.println("----- update() -----");
        baron.setEmail("baron@gmail.com");
        userService.update(baron.getId(), baron);

        // 수정된 데이터 조회
        System.out.println("----- 수정된 사용자 조회 -----");
        System.out.println(userService.findById(baron.getId()));

        // 삭제
        System.out.println("----- delete() -----");
        userService.delete(baron.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("----- 삭제 후 조회 -----");
        List<User> deletedUserList = userService.findAll();
        deletedUserList.forEach(System.out::println);

        /* ========================= Channel ========================= */
        System.out.println();
        System.out.println("===== Channel =====");
        // 등록
        System.out.println("----- create() -----");
        Channel freeroom = channelService.create(new Channel("자유방", "자유롭게 채팅하는 방입니다."));
        Channel noticeroom = channelService.create(new Channel("공지방", "공지사항을 올리는 방입니다."));

        // 단건 조회
        System.out.println("----- findById() -----");
        System.out.println(channelService.findById(freeroom.getId()));
        System.out.println(channelService.findById(noticeroom.getId()));

        // 다건 조회
        System.out.println("----- findAll() -----");
        List<Channel> channelList = channelService.findAll();
        channelList.forEach(System.out::println);

        // 수정
        System.out.println("----- update() -----");
        noticeroom.setDescription("공지방입니다. 공지를 확인해 주세요.");
        channelService.update(noticeroom.getId(), noticeroom);

        // 수정된 데이터 조회
        System.out.println("----- 수정된 채널 조회 -----");
        System.out.println(channelService.findById(noticeroom.getId()));

        // 삭제
        System.out.println("----- delete() -----");
        channelService.delete(noticeroom.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("----- 삭제 후 조회 -----");
        List<Channel> deletedChannelList = channelService.findAll();
        deletedChannelList.forEach(System.out::println);

        /* ========================= Message ========================= */
        System.out.println();
        System.out.println("===== Message =====");
        // 등록
        System.out.println("----- create() -----");
        Message hiMessage = messageService.create(new Message("안녕하세요. 하이하이", aaron.getId(), freeroom.getId()));
        Message byeMessage = messageService.create(new Message("안녕히 계세요. 바이바이", caron.getId(), freeroom.getId()));
        Message notice = messageService.create(new Message("공지사항 안내드립니다.", aaron.getId(), noticeroom.getId()));

        // 단건 조회
        System.out.println("----- findById() -----");
        System.out.println(messageService.findById(hiMessage.getId()));
        System.out.println(messageService.findById(notice.getId()));

        // 다건 조회
        System.out.println("----- findAll() -----");
        List<Message> messageList = messageService.findAll();
        messageList.forEach(System.out::println);

        // 수정
        System.out.println("----- update() -----");
        byeMessage.setContent("안녕히 계세요. 바이바이. 바이짜이찌엔");
        messageService.update(byeMessage.getId(), byeMessage);

        // 수정된 데이터 조회
        System.out.println("----- 수정된 채널 조회 -----");
        System.out.println(messageService.findById(byeMessage.getId()));

        // 삭제
        System.out.println("----- delete() -----");
        messageService.delete(notice.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("----- 삭제 후 조회 -----");
        List<Message> deletedMessageList = messageService.findAll();
        deletedMessageList.forEach(System.out::println);
    }

}
