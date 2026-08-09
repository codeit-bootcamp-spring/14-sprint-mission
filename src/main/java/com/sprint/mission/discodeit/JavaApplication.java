package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 0. 서비스 객체 생성 및 조립 (의존성 주입)
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        // 메세지 서비스 생성자를 사용하여 메인에서 서비스들을 조립해 줍니다.
        MessageService messageService = new JCFMessageService(userService, channelService);


        // 1. 데이터 등록 (Create)
        System.out.println("====== 데이터 등록 테스트 ======");
        User user = userService.createUser("홍길동", "hong@test.com");
        Channel channel = channelService.createChannel("자바-공부방", "열공하는 방입니다.");

        //  수정한 파라미터 규칙 적용 (콘텐츠, 유저ID, 채널ID)
        Message message = messageService.createMessage("안녕하세요! 첫 메시지입니다.", user.getId(), channel.getId());

        System.out.println("유저 생성 완료: " + user.getNickName() + "/ ID: " + user.getId() + "");
        System.out.println("채널 생성 완료: " + channel.getName());
        System.out.println("메시지 생성 완료: " + message.getContent() + "\n");


        // 2. 데이터 조회 (상세 조회, 전체 조회)
        System.out.println("====== 데이터 조회 테스트 ======");
        User foundUser = userService.readUser(user.getId());
        List<User> allUsers = userService.readAllUsers();

        System.out.println("단건 유저 조회 성공: " + foundUser.getNickName());
        System.out.println("전체 유저 수: " + allUsers.size() + "명\n");


        // 3. 심화 요구사항 테스트 (가짜 ID 검증)
        System.out.println("====== 심화 요구사항 검증 (존재하지 않는 유저 예외 처리) ======");
        try {
            UUID fakeUserId = UUID.randomUUID(); // 가짜 유저 ID 생성
            System.out.println("존재하지 않는 유저 ID로 메시지 생성을 시도합니다...");

            // 가짜 ID를 넣고 생성을 시도
            messageService.createMessage("이 메시지는 저장되면 안 됩니다.", fakeUserId, channel.getId());
            System.out.println("테스트 실패: 예외가 발생하지 않고 메시지가 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            // throw new IllegalArgumentException() 처리를 잘 하셨다면 이 블록으로 들어옵니다.
            System.out.println("테스트 성공 (정상 차단): " + e.getMessage() + "\n");
        }

        // 4. 데이터 수정 및 수정 확인
        System.out.println("======  데이터 수정 테스트 ======");
        userService.updateUser(user.getId(), "이순신", "lee@test.com");

        // 다시 조회하여 반영되었는지 확인
        User updatedUser = userService.readUser(user.getId());
        System.out.println("수정 후 조회 결과: 이름이 " + updatedUser.getNickName() + "으로 변경되었습니다.\n");


        // 5. 데이터 삭제 및 삭제 확인
        System.out.println("======  데이터 삭제 테스트 ======");
        UUID deletedUserId = user.getId();
        System.out.println("유저 데이터를 삭제합니다.");
        userService.deleteUser(deletedUserId);

        // 조회를 통해 정말 지워졌는지 확인 (예외가 정상적으로 터지는지 테스트)
        try {
            userService.readUser(deletedUserId);
            System.out.println("테스트 실패: 삭제된 유저가 여전히 조회됩니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(" 테스트 성공 (정상 삭제 확인): " + e.getMessage() + "\n");
        }

    }
}
