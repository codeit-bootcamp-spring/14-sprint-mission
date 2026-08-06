package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ServiceFactory {
    //필드정의
    private UserService userService;
    private ChannelService channelService;
    private MessageService messageService;
    //싱글톤 생성
    private static final ServiceFactory singleton = new ServiceFactory();

    //이제 ServiceFactory생성하면 자등으로 모든 서비스의 객체 생성되며 사용가능
    public ServiceFactory() {
        this.userService = new FileUserService();
        this.channelService = new FileChannelService(userService);
        this.messageService = new FileMessageService(channelService, userService);
    }

    public static ServiceFactory useSingleton() {
        return singleton;
    }
}
