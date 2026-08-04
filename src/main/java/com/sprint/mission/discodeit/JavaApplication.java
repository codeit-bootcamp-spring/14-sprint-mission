package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicServiceFactory;

import java.util.List;

public class JavaApplication {

    static UserResponseDto setupUser(UserService userService) {
        UserCreateRequestDto requestDto = new UserCreateRequestDto("woody@codeit.com", "woody");
        return userService.createUser(requestDto);
    }

    static ChannelResponseDto setupChannel(ChannelService channelService, UserResponseDto userDto) {
        ChannelCreateRequestDto requestDto = new ChannelCreateRequestDto("공지", List.of(userDto.getId()));
        return channelService.createChannel(requestDto);
    }

    static void messageCreateTest(MessageService messageService, ChannelResponseDto channelDto, UserResponseDto userDto) {
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("안녕하세요.", channelDto.getId(), userDto.getId());
        MessageResponseDto responseDto = messageService.createMessage(requestDto);

        System.out.println("메시지 생성 성공! Message ID: " + responseDto.getId());
        System.out.println("메시지 내용: " + responseDto.getValues());
        System.out.println("보낸 사람 ID: " + responseDto.getSenderId());
        System.out.println("채널 ID: " + responseDto.getChannelId());
    }

    public static void main(String[] args) {
        BasicServiceFactory factory = new BasicServiceFactory();

        UserService userService = factory.getUserService();
        ChannelService channelService = factory.getChannelService();
        MessageService messageService = factory.getMessageService();

        UserResponseDto userDto = setupUser(userService);
        ChannelResponseDto channelDto = setupChannel(channelService, userDto);

        System.out.println("=== DTO 기반 서비스 템플릿 테스트 시작 ===");
        messageCreateTest(messageService, channelDto, userDto);
        System.out.println("=== 템플릿 테스트 완료 ===");
    }
}
