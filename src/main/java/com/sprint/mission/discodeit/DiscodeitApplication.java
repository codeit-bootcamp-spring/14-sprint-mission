package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

/** 객체를 직접 만들지 않고 Spring이 만들어 둔 Bean을 context에서 꺼내 쓴다. */
@SpringBootApplication
public class DiscodeitApplication {

    static UserDto setupUser(UserService userService) {
        return userService.create(
                new UserCreateRequest("woody", "woody@codeit.com", "woody1234"),
                Optional.empty()
        );
    }

    static Channel setupChannel(ChannelService channelService) {
        return channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
    }

    static void messageCreateTest(MessageService messageService, Channel channel, UserDto author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.id());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 구현체 이름을 모른 채 인터페이스로만 요청한다.
        // 무엇이 주입될지는 @Service·@Repository가 붙은 위치가 정한다.
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UserDto user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);

        advancedUserServiceTest(context, userService);
    }

    /** UserService 고도화 요구사항이 실제로 동작하는지 확인. */
    private static void advancedUserServiceTest(ConfigurableApplicationContext context, UserService userService) {
        BinaryContentRepository binaryContentRepository = context.getBean(BinaryContentRepository.class);
        UserStatusRepository userStatusRepository = context.getBean(UserStatusRepository.class);

        System.out.println("\n===== UserService 고도화 =====");

        // 1. 프로필 이미지를 같이 등록
        BinaryContentCreateRequest profile = new BinaryContentCreateRequest(
                "profile.png", "image/png", "가짜-이미지-바이트".getBytes(StandardCharsets.UTF_8));
        UserDto withProfile = userService.create(
                new UserCreateRequest("sungjun", "sungjun@codeit.com", "pw1234"),
                Optional.of(profile));
        System.out.println("1. 프로필과 함께 등록  : profileId=" + withProfile.profileId());

        // 2. 조회하면 온라인 상태가 붙고, password는 애초에 DTO에 없다
        UserDto found = userService.find(withProfile.id());
        System.out.println("2. 조회 결과          : username=" + found.username()
                + ", online=" + found.online() + ", 필드에 password 없음");

        // 3. username 중복은 막는다
        try {
            userService.create(new UserCreateRequest("sungjun", "other@codeit.com", "pw"), Optional.empty());
            System.out.println("3. 중복 등록          : 막히지 않음 (요구사항 위반)");
        } catch (IllegalArgumentException e) {
            System.out.println("3. 중복 등록          : 거부됨 - " + e.getMessage());
        }

        // 4. 필드 수정 — username만 바꾸고 나머지는 null로 넘겨 유지되는지 본다.
        userService.update(
                withProfile.id(),
                new UserUpdateRequest("sungjun2", null, null),
                Optional.empty());
        // 반환값이 아니라 저장소에서 다시 꺼내야 "저장까지 됐는지"가 확인된다.
        UserDto refetched = userService.find(withProfile.id());
        System.out.println("4. 수정 후 재조회      : username=" + refetched.username()
                + " (바뀜), email=" + refetched.email()
                + " (null로 넘겨서 유지), updatedAt=" + (refetched.updatedAt() != null ? "기록됨" : "없음"));

        // 5. 프로필을 대체하면 이전 BinaryContent는 지워진다
        UUID oldProfileId = refetched.profileId();
        UserDto updated = userService.update(
                withProfile.id(),
                new UserUpdateRequest(null, null, null),
                Optional.of(new BinaryContentCreateRequest(
                        "new.png", "image/png", "새-이미지".getBytes(StandardCharsets.UTF_8))));
        System.out.println("5. 프로필 교체        : " + oldProfileId + " -> " + updated.profileId()
                + " / 이전 것 남아있나=" + binaryContentRepository.findById(oldProfileId).isPresent());

        // 6. 삭제하면 UserStatus와 프로필도 같이 사라진다
        UUID deletedId = updated.id();
        UUID lastProfileId = updated.profileId();
        userService.delete(deletedId);
        System.out.println("6. 삭제 후            : UserStatus 남아있나="
                + userStatusRepository.findByUserId(deletedId).isPresent()
                + ", 프로필 남아있나=" + binaryContentRepository.findById(lastProfileId).isPresent());

        System.out.println("전체 유저 수          : " + userService.findAll().size());
    }

}
