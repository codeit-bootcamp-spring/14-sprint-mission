package com.sprint.mission.discodeit.domain.service.application;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelType;
import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.entity.ReadStatus;

import com.sprint.mission.discodeit.domain.service.channel.ChannelService;
import com.sprint.mission.discodeit.domain.service.message.MessageService;
import com.sprint.mission.discodeit.domain.service.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelPublicCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.req.ChannelPrivateCreateRequestDTO;
import com.sprint.mission.discodeit.web.controller.dto.res.ChannelFindResponseDTO;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.web.controller.dto.res.ChannelResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/*

 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ChannelServiceApp {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;
    private final MessageService messageService;
    private final UserService userService;

    //퍼블릭 채널 저장
    public ChannelResponseDTO makePublicChannel(ChannelPublicCreateRequestDTO channelPublicCreateRequestDTO) {
        Channel channel = Channel.init(channelPublicCreateRequestDTO.name(), ChannelType.PUBLIC_CHANNEL, channelPublicCreateRequestDTO.description());
        channelService.makeChannel(channel);

        return ChannelResponseDTO.from(channel);
    }

    //프라이빗 채널 저장
    public ChannelResponseDTO makePrivateChannel(
        ChannelPrivateCreateRequestDTO channelPrivateCreateRequestDTO) {
        // todo : api 규격에 맞추다보니 이상해짐... 오버로딩으로 빼야할듯
        Channel channel = Channel.init(null, ChannelType.PRIVATE_CHANNEL, null);

        /*
            1. 채널저장
            2. 유저아이디 리스트로 들어온 것 + 채널 아이디 정보로 리드스테이터스 개체 일일이 생성
            3. 생성된 모든 개체 리드스테이터스 저장
         */
        Channel madeChannel = channelService.makeChannel(channel);


        List<UUID> userIdList = channelPrivateCreateRequestDTO.participantIds();
        /*
            이 검증을 유저 도메인 서비스 안에 두면 도메인 서비스가 요구사항에 종속되는 것이라 느껴져 앱서비스에서 예외처리를 하도록함
         */
        log.info("유저 리스트 정보 {}", userIdList);
        if(!userService.existAllByIdList(userIdList)){
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }
        // todo : 뭐지? fix 해야함  --------- ReadStatus 여기서 생성하는게 맞나? 지금 일단 여기서 now 로 생성하게 임시조치
        List<ReadStatus> readStatuses = userIdList.stream()
            .map(userId -> ReadStatus.init(userId, madeChannel.getId(), Instant.now())
            )
            .toList();

        for (ReadStatus readStatus : readStatuses) {
            readStatusService.createReadStatus(readStatus);
        }

        return ChannelResponseDTO.from(madeChannel);
    }

    //특정 채널을 조회하고 싶을 때
    public ChannelFindResponseDTO findChannel(UUID channelId) {
        /*
            1. 채널 찾기
            2. 프라이빗이라면 그 채널에 참여한 유저들 찾기
            3. 메시지 서비스에서 해당 채널의 마지막 메시지 찾기
         */
        Channel channel = channelService.findChannelById(channelId);

        List<UUID> userIdList = null;
        if(channel.isPrivate()){
            List<ReadStatus> readStatuses = readStatusService.findAllReadStatusByChannelId(channelId);
            userIdList = readStatuses.stream()
                .map(ReadStatus::getUserId)
                .toList();
        }


        Instant createdAt = null;
        Optional<Message> message = messageService.findLastMessageByChannelId(channel.getId());
        if(message.isPresent()){
            createdAt = message.get().getCreatedAt();
        }

        return ChannelFindResponseDTO.builder()
            .channelId(channel.getId()).channelName(channel.getChannelName())
            .channelType(channel.getChannelType()).latestMessageAt(createdAt)
            .userIdList(userIdList).build();
    }

    //dto 없이 단일 인자만 받음
    public void deleteChannel(UUID channelId){
        //삭제가 되지않는건
        messageService.deleteMessageByChannelId(channelId);
        readStatusService.deleteReadStatusByChannelId(channelId);
        channelService.deleteChannel(channelId);
    }

    public List<ChannelResponseDTO> findAllChannelByUserId(UUID userId){
        userService.findUserById(userId);
        List<ReadStatus> readStatusList = readStatusService.findReadStatusByUserId(userId);

        List<UUID> joinedChannelIds = readStatusList.stream()
            .map(ReadStatus::getChannelId)
            .toList();

        List<Channel> allPublicChannel = channelService.findAllPublicChannel();
        List<Channel> allJoinedPrivateChannel = channelService.findAllChannelByIds(joinedChannelIds);

        List<Channel> accessibleChannels = Stream.of(allPublicChannel, allJoinedPrivateChannel)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        return accessibleChannels.stream()
            .map(ChannelResponseDTO::from)
            .toList();
    }
}
