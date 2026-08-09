package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.Getter;

@Getter
public class BasicServiceFactory {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    public BasicServiceFactory() {
        UserRepository userRepository = new FileUserRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();

        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();

        this.userService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
        this.channelService = new BasicChannelService(channelRepository, userService, readStatusRepository, messageRepository);
        this.messageService = new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);
        this.readStatusService = new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);
    }
}