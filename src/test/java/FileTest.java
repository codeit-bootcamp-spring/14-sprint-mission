import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
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
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class FileTest {
    UserService userService = new BasicUserService(new FileUserRepository());
    ChannelService channelService = new BasicChannelService(new FileChannelRepository());
    MessageService messageService = new BasicMessageService(new FileMessageRepository());

    @Test
    void create_테스트(){
        User user = userService.create("김양현","yyy2724@naver.com","2724");
        Channel channel = channelService.create(ChannelType.PUBLIC,"공지","공지123");
        Message message = messageService.create("공지", channel.getId(),user.getId());
    }

    @Test
    void read_테스트(){
        System.out.println(userService.read(UUID.fromString("5e8d729b-23c0-4407-8b01-759132f24676")));
        System.out.println(channelService.read(UUID.fromString("1a3aec7d-ff34-4803-8755-b7d069d601c9")));
        System.out.println(messageService.read(UUID.fromString("7291750d-b912-403d-b75f-c0750c89c905")));
    }

    @Test
    void update_테스트(){
        User user = userService.read(UUID.fromString("5e8d729b-23c0-4407-8b01-759132f24676"));
        user.update("김양횬", "new@test.com", "9999");
        userService.update(user);

        Channel channel = channelService.read(UUID.fromString("1a3aec7d-ff34-4803-8755-b7d069d601c9"));
        channel.update(ChannelType.PRIVATE,"바뀐공지","바뀐공지123");
        channelService.update(channel);

        Message message = messageService.read(UUID.fromString("7291750d-b912-403d-b75f-c0750c89c905"));
        message.update("바뀐공지");
        messageService.update(message);
    }

    @Test
    void delete_테스트(){
        userService.delete(UUID.fromString("ed5ab5b4-63e7-4669-a91d-9b52b7878914"));
        channelService.delete(UUID.fromString("5966ca98-f44c-4ba8-8385-aab57fc0d2fa"));
        messageService.delete(UUID.fromString("ad9ce174-7b06-455d-9628-e101b1de0d68"));
    }

    @Test
    void findAll_테스트(){
        System.out.println(userService.readAll());
        System.out.println(channelService.readAll());
        System.out.println(messageService.readAll());
    }
}
