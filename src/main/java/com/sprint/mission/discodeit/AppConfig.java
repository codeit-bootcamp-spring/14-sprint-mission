package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.Optional;
import java.util.function.Supplier;

public class AppConfig {
    private static UserRepository userRepository;
    private static ChannelRepository channelRepository;
    private static MessageRepository messageRepository;

    private static UserService userService;
    private static ChannelService channelService;
    private static MessageService messageService;

    public static UserRepository userRepository() {
        return get(userRepository, () -> userRepository = new FileUserRepository("src/main/java/com/sprint/mission/discodeit/repository/file/data/user.ser"));
    }

    public static ChannelRepository channelRepository() {
        return get(channelRepository, () -> channelRepository = new FileChannelRepository("src/main/java/com/sprint/mission/discodeit/repository/file/data/channel.ser"));
    }

    public static MessageRepository messageRepository() {
        return get(messageRepository, () -> messageRepository = new FileMessageRepository("src/main/java/com/sprint/mission/discodeit/repository/file/data/message.ser"));
    }

    public static UserService userService() {
        return get(userService, () -> userService = new BasicUserService(userRepository(), messageRepository(), channelRepository()));
    }

    public static ChannelService channelService() {
        return get(channelService, () -> channelService = new BasicChannelService(channelRepository() ,messageRepository(),userRepository()));
    }

    public static MessageService messageService() {
        return get(messageService, () -> messageService = new BasicMessageService(messageRepository(), channelRepository()));
    }

    private static <T> T get(T t, Supplier<T> supplier) {
        return Optional.ofNullable(t)
                .orElseGet(supplier);
    }
}
