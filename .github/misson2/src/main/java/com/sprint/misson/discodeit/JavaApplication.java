package com.sprint.misson.discodeit;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.file.FileChannelRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.misson.discodeit.service.ChannelService;
import com.sprint.misson.discodeit.service.MessageService;
import com.sprint.misson.discodeit.service.UserService;
import com.sprint.misson.discodeit.service.file.FileChannelService;
import com.sprint.misson.discodeit.service.jcf.JCFChannelService;
import com.sprint.misson.discodeit.service.jcf.JCFMessageService;
import com.sprint.misson.discodeit.service.jcf.JCFUserService;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        ChannelService channelService = new JCFChannelService(new JCFChannelRepository());
//        ChannelService channelFileService = new FileChannelService(new FileChannelRepository());
        UserService userService = new JCFUserService(new JCFUserRepository());
        MessageService messageService = new JCFMessageService(new JCFMessageRepository());
        Channel channel = channelService.create(ChannelType.PUBLIC, "스프린트");
        User user = userService.create("스프린터1", "aa@aa.aa");
        User user2 = userService.create("스프린터111", "bb@aa.aaa");
        User user3 = userService.create("스프린터111", "cc@aa.aaaa");
        Message message = messageService.create("첫 커밋", user, channel);
        Message message2 = messageService.create("첫 커밋", user, channel);
        Message message3 = messageService.create("첫 커밋", user, channel);
        Message message4 = messageService.create("첫 커밋", user, channel);
        List<Message> list = messageService.readByUser(user);
        userService.delete("aa@aa.aa");
        messageService.update(message, "수정됨");
        messageService.delete(message);
        messageService.readAll();
        userService.readAll();
        channelService.readAll();
    }
}
