package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.IService.ChannelService;
import com.sprint.mission.discodeit.service.IService.MessageService;
import com.sprint.mission.discodeit.service.IService.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
        static User setupUser(UserService userService) {
                User user = new User("민준", "minjun@naver.com");
                userService.create(user);
                return user;
        }

        static Channel setupChannel(ChannelService channelService) {
                Channel channel = new Channel("GongJi");
                channelService.create(channel);
                return channel;
        }

        static void messageCreateTest(MessageService messageService, Channel channel, User author) {
                Message message = new Message("안녕하세요.", author.getId(),channel.getId());
                messageService.create(message);
                System.out.println("메시지 생성: " + message);
        }
        static void runTest(UserRepository userRepository,
            ChannelRepository channelRepository,
            MessageRepository messageRepository) {
                UserService userService = new BasicUserService(userRepository);
                ChannelService channelService = new BasicChannelService(channelRepository);
                MessageService messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);

                User user = setupUser(userService);
                Channel channel = setupChannel(channelService);
                messageCreateTest(messageService, channel, user);
        }

    static void main() {
            System.out.println("===== JCF 기반 테스트 =====");
            runTest(new JCFUserRepository(), new JCFChannelRepository(), new JCFMessageRepository());

            System.out.println("===== File 기반 테스트 =====");
            runTest(new FileUserRepository(), new FileChannelRepository(), new FileMessageRepository());
    }

    }


