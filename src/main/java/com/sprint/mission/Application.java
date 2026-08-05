package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
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
        /* ========================= JCF ========================= */
        UserService userService = new JCFUserService(new JCFUserRepository());
        ChannelService channelService = new JCFChannelService(new JCFChannelRepository());
        MessageService messageService = new JCFMessageService(new JCFMessageRepository());

        /* ========================= File ========================= */
        UserService fileUserService = new FileUserService(new FileUserRepository());
        ChannelService fileChannelService = new FileChannelService(new FileChannelRepository());
        MessageService fileMessageService = new FileMessageService(new FileMessageRepository());

        /* ========================= JCF ========================= */
        /* ========================= User ========================= */
        System.out.println("===== JCF =====");
        System.out.println("===== User =====");
        // 등록
        System.out.println("----- create() -----");
        User aaron = userService.create(new User("Aaron", 20, "aaron@naver.com"));
        User baron = userService.create(new User("Baron", 30, "baron@naver.com"));
        User caron = userService.create(new User("Caron", 40, "caron@naver.com"));

        // 단건 조회
        System.out.println("\n----- findById() -----");
        System.out.println(userService.findById(aaron.getId()));
        System.out.println(userService.findById(caron.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<User> userList = userService.findAll();
        userList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        baron.setEmail("baron@gmail.com");
        userService.update(baron.getId(), baron);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 사용자 조회 -----");
        System.out.println(userService.findById(baron.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        userService.delete(baron.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
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
        System.out.println("\n----- findById() -----");
        System.out.println(channelService.findById(freeroom.getId()));
        System.out.println(channelService.findById(noticeroom.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<Channel> channelList = channelService.findAll();
        channelList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        noticeroom.updateDescription("공지방입니다. 공지를 확인해 주세요.");
        channelService.update(noticeroom.getId(), noticeroom);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 채널 조회 -----");
        System.out.println(channelService.findById(noticeroom.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        channelService.delete(noticeroom.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
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
        System.out.println("\n----- findById() -----");
        System.out.println(messageService.findById(hiMessage.getId()));
        System.out.println(messageService.findById(notice.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<Message> messageList = messageService.findAll();
        messageList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        byeMessage.updateContent("안녕히 계세요. 바이바이. 바이짜이찌엔");
        messageService.update(byeMessage.getId(), byeMessage);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 채널 조회 -----");
        System.out.println(messageService.findById(byeMessage.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        messageService.delete(notice.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
        List<Message> deletedMessageList = messageService.findAll();
        deletedMessageList.forEach(System.out::println);

        /* ========================= File ========================= */
        /* ========================= User ========================= */
        System.out.println("===== File =====");
        System.out.println("===== User =====");
        // 등록
        System.out.println("----- create() -----");
        User daron = fileUserService.create(new User("Daron", 20, "daron@naver.com"));
        User earon = fileUserService.create(new User("Earon", 30, "earon@naver.com"));
        User faron = fileUserService.create(new User("Faron", 40, "faron@naver.com"));

        // 단건 조회
        System.out.println("\n----- findById() -----");
        System.out.println(fileUserService.findById(daron.getId()));
        System.out.println(fileUserService.findById(faron.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<User> fileUserList = fileUserService.findAll();
        fileUserList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        earon.setEmail("earon@gmail.com");
        fileUserService.update(earon.getId(), earon);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 사용자 조회 -----");
        System.out.println(fileUserService.findById(earon.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        fileUserService.delete(earon.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
        List<User> fileDeletedUserList = fileUserService.findAll();
        fileDeletedUserList.forEach(System.out::println);

        /* ========================= Channel ========================= */
        System.out.println();
        System.out.println("===== Channel =====");
        // 등록
        System.out.println("----- create() -----");
        Channel fileFreeroom = fileChannelService.create(new Channel("자유방", "자유롭게 채팅하는 방입니다."));
        Channel fileNoticeroom = fileChannelService.create(new Channel("공지방", "공지사항을 올리는 방입니다."));

        // 단건 조회
        System.out.println("\n----- findById() -----");
        System.out.println(fileChannelService.findById(fileFreeroom.getId()));
        System.out.println(fileChannelService.findById(fileNoticeroom.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<Channel> fileChannelList = fileChannelService.findAll();
        fileChannelList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        fileNoticeroom.updateDescription("공지방입니다. 공지를 확인해 주세요.");
        fileChannelService.update(fileNoticeroom.getId(), fileNoticeroom);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 채널 조회 -----");
        System.out.println(fileChannelService.findById(fileNoticeroom.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        fileChannelService.delete(fileNoticeroom.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
        List<Channel> fileDeletedChannelList = fileChannelService.findAll();
        fileDeletedChannelList.forEach(System.out::println);

        /* ========================= Message ========================= */
        System.out.println();
        System.out.println("===== Message =====");
        // 등록
        System.out.println("----- create() -----");
        Message fileHiMessage = fileMessageService.create(new Message("안녕하세요. 하이하이", daron.getId(), fileFreeroom.getId()));
        Message fileByeMessage = fileMessageService.create(new Message("안녕히 계세요. 바이바이", earon.getId(), fileFreeroom.getId()));
        Message fileNotice = fileMessageService.create(new Message("공지사항 안내드립니다.", daron.getId(), fileNoticeroom.getId()));

        // 단건 조회
        System.out.println("\n----- findById() -----");
        System.out.println(fileMessageService.findById(fileHiMessage.getId()));
        System.out.println(fileMessageService.findById(fileNotice.getId()));

        // 다건 조회
        System.out.println("\n----- findAll() -----");
        List<Message> fileMessageList = fileMessageService.findAll();
        fileMessageList.forEach(System.out::println);

        // 수정
        System.out.println("\n----- update() -----");
        fileByeMessage.updateContent("안녕히 계세요. 바이바이. 바이짜이찌엔");
        fileMessageService.update(fileByeMessage.getId(), fileByeMessage);

        // 수정된 데이터 조회
        System.out.println("\n----- 수정된 채널 조회 -----");
        System.out.println(fileMessageService.findById(fileByeMessage.getId()));

        // 삭제
        System.out.println("\n----- delete() -----");
        fileMessageService.delete(fileNotice.getId());

        // 조회를 통해 삭제되었는지 확인
        System.out.println("\n----- 삭제 후 조회 -----");
        List<Message> fileDeletedMessageList = fileMessageService.findAll();
        fileDeletedMessageList.forEach(System.out::println);
    }
}
