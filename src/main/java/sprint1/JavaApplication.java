package sprint1;

import static com.example.demo.levelTest1.entity.ChannelType.PRIVATE;

import com.example.demo.levelTest1.JCF.JCFChannelService;
import com.example.demo.levelTest1.JCF.JCFMessageService;
import com.example.demo.levelTest1.JCF.JCFUserService;
import com.example.demo.levelTest1.entity.Channel;
import com.example.demo.levelTest1.entity.ChannelType;
import com.example.demo.levelTest1.entity.Message;
import com.example.demo.levelTest1.entity.User;
import com.example.demo.levelTest1.service.ChannelService;
import com.example.demo.levelTest1.service.MessageService;
import com.example.demo.levelTest1.service.UserService;
import java.util.List;
import java.util.UUID;

public class JavaApplication {

    static void userCRUDTest(UserService userService) {

        // 생성
        User user1 = userService.create("giseo", "aqzz2000@naver.com", "1234");
        System.out.println("새로운 유저 생성: " + user1);

        //조회
        User foundUser = userService.find(user1.getId());
        System.out.println("유저 조회(단건): " + foundUser);
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());

        //수정
        User updatedUser = userService.update(user1.getId(), "gisao");
        System.out.println("회원 아이디 수정: " + updatedUser.getUsername());

        // 삭제
        userService.delete(user1.getId());
        List<User> foundUserAfterDelete = userService.findAll();
        System.out.println("유저 삭제: " + foundUserAfterDelete.size());
    }


    static void channelCRUDTest(ChannelService channelService) {
        //생성
        ChannelType type = ChannelType.PUBLIC;
        Channel channel = channelService.create(type, "giseo", "기서채널이야");
        System.out.println(" " + channel);

        // 조회
        Channel foundChannel = channelService.find(channel.getId());
        System.out.println("채널 조회(단건): " + foundChannel.getId());
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());

        //수정
        Channel updatedChannel = channelService.update(channel.getId(), PRIVATE);
        System.out.println("채널 타입 수정:" + updatedChannel.getType());

        //삭제
        channelService.delete(channel.getId());
        List<Channel> foundChannelAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelAfterDelete.size());
    }


    static void messageCRUDTest(MessageService messageService) {

        // 생성
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Message message = messageService.create("안녕하세요.", channelId, authorId);
        System.out.println("메시지 생성: " + message.getId());
        // 조회
        Message foundMessage = messageService.find(message.getId());
        System.out.println("메시지 조회(단건): " + foundMessage.getId());
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건):" + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.update(message.getId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getContent());
        //삭제
        messageService.delete(message.getId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
    }


    public static void main(String[] args) {
        // 서비스 초기화
        MessageService messageService = new JCFMessageService();
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        // 테스트
        messageCRUDTest(messageService);
        userCRUDTest(userService);
        channelCRUDTest(channelService);
    }
}





