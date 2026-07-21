package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Objects;

public class JCFMessageService extends JCFService<Message> implements MessageService {

    ChannelService channelService;
    UserService userService;


    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public void create(Message message) {
        if (Objects.isNull(channelService.read(message.getUser_id()))) {
            throw new RuntimeException("없는 채널Id입니다");
        }
        if (Objects.isNull(userService.read(message.getChannel_id()))) {
            throw new RuntimeException("없는 유저 Id입니다.");
        }
        entityFile.put(message.getId(), message);
    }

    @Override
    public void update(Message message, String newtext) {
        Message existing = read(message.getId());
        existing.setText(newtext);
    }


}
