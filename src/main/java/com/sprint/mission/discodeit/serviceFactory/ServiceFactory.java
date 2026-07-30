package com.sprint.mission.discodeit.serviceFactory;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import lombok.Getter;

@Getter
// 팩토리 패턴 이용해서 모든 Service 생성, JCF/FileRepository 생성 - 사용자(Main)에게 Repository 만들어지는 과정 캡슐화
public class ServiceFactory {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    private ServiceFactory(RepositoryType repositoryType) {  // 디폴트가 JCF

        UserRepository userRepository;
        ChannelRepository channelRepository;
        MessageRepository messageRepository;

        switch (repositoryType) {
            case JCF -> {
                userRepository = JCFUserRepository.getInstance();
                channelRepository = JCFChannelRepository.getInstance();
                messageRepository = JCFMessageRepository.getInstance();
            }
            case FILE -> {
                userRepository = FileUserRepository.getInstance();
                channelRepository = FileChannelRepository.getInstance();
                messageRepository = FileMessageRepository.getInstance();
            }
            default -> {
                throw new IllegalArgumentException("지원하지 않는 저장 방식입니다. JCF 또는 FILE을 입력하세요.");
            }
        }

        this.userService = new BasicUserService(userRepository);
        this.channelService = new BasicChannelService(channelRepository);
        this.messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);

    }

    // QQQ: ServiceFactory가 argument를 받아서 LazyHolder 못 쓰는데.. -> 없어도 싱글턴 ok?
//    private static class LazyHolder {
//        private static final ServiceFactory serviceFactory = new ServiceFactory();
//    }

    public static ServiceFactory createFactory(RepositoryType repositoryType) {
        return new ServiceFactory(repositoryType);
    }
}
