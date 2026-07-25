package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.Getter;
@Getter
public class BasicServiceFactory {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    public BasicServiceFactory() {

        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();
        this.userService = new BasicUserService(userRepository);
        this.channelService = new BasicChannelService(channelRepository, userService);
        this.messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
    }
}