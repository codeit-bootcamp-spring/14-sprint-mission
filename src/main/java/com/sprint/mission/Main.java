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

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {

    static void main() {
UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(channelService, userService);

        User user1 = new User("민준", "minjum00@naver.com");
        User user2 = new User("철수", "chul@naver.com");
        User user3 = new User("영희,", "0hee@naver.com");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        System.out.println("----유저2를 탐색합니다.----");
        System.out.println(userService.read(user2.getId()));

        System.out.println("----전체를 조회합니다.----");
        System.out.println(userService.findAll());

        userService.update(user1, "민국", "kuk@naver.com");

        System.out.println("----업데이된 내용 조회----");
        System.out.println(user1);

        userService.delete(user3.getId());

        System.out.println("----삭제된 내용 조회----");
        System.out.println(userService.findAll());

        System.out.println("-------채널 서비스 -------");

        Channel ch1 = new Channel("OO channel");
        Channel ch2 = new Channel("XX channel");
        Channel ch3 = new Channel("TT channel");

        channelService.create(ch1);
        channelService.create(ch2);
        channelService.create(ch3);

        System.out.println("----ch2를 탐색합니다.----");
        System.out.println(channelService.read(ch2.getId()));

        System.out.println("----전체를 조회합니다.----");
        System.out.println(channelService.findAll());

        channelService.update(ch1, "YY channel");

        System.out.println("----업데이된 내용 조회----");
        System.out.println(ch1);

        channelService.delete(ch3.getId());

        System.out.println("----삭제된 내용 조회----");
        System.out.println(channelService.findAll());

        System.out.println("-------메시지 서비스 -------");

        Message msg1 = new Message("안녕하세요", ch1.getId(), user1.getId());
        Message msg2 = new Message("반갑습니다.", ch1.getId(), user2.getId());
        Message msg3 = new Message("오늘 하루 어떠세요", ch1.getId(), user1.getId());

        messageService.create(msg1);
        messageService.create(msg2);
        messageService.create(msg3);

        System.out.println("----메시지2를 탐색합니다.----");
        System.out.println(messageService.read(msg2.getId()));

        System.out.println("----전체를 조회합니다.----");
        System.out.println(messageService.findAll());

        messageService.update(msg1, "하하하하");

        System.out.println("----업데이된 내용 조회----");
        System.out.println(msg1);

        messageService.delete(msg3.getId());

        System.out.println("----삭제된 내용 조회----");
        System.out.println(messageService.findAll());

    }

}
