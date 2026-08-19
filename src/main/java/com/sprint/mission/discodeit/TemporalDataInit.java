package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelType;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.service.channel.ChannelService;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.domain.service.userstatus.UserStatusService;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
    todo : 포스트맨 임시 테스트용 - 추후 삭제
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TemporalDataInit {
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final ChannelService channelService;

    @PostConstruct
    public void init() {
        UUID defaultId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        User testUser = User.init("tester", "1234", "홍길동", 25);

        UUID defaultId2 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        User testUser2 = User.init("tester2", "1234", "홍길동", 25);

        try{
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, defaultId);
        }catch (Exception e){
            log.error("sad",e);
        }

        try{
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser2, defaultId2);
        }catch (Exception e){
            log.error("sad",e);
        }

        userService.createUser(testUser);
        userService.createUser(testUser2);
        log.info("생성 완료 {}", testUser.getId());

        UserStatus userStatus = UserStatus.init(testUser.getId());
        UserStatus userStatus2 = UserStatus.init(testUser2.getId());
        userStatusService.createUserStatus(userStatus   );
        userStatusService.createUserStatus(userStatus2);
        log.info("스테이터스 생성 완료 {}", userStatus.getUserId());



        Channel tempChannel = Channel.init("test", ChannelType.PUBLIC_CHANNEL);
        try{
            Field idField = Channel.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(tempChannel, defaultId);
        }catch (Exception e){
            log.error("sad",e);
        }

        channelService.makeChannel(tempChannel);
    }
}
