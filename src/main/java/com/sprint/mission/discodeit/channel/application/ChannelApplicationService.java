package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.channel.domain.entity.ChannelType;
import com.sprint.mission.discodeit.channel.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelUpdateRequestDTO;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.message.domain.repository.MessageRepository;
import com.sprint.mission.discodeit.message.domain.repository.MessageRepository.ChannelLastMessage;
import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;

import com.sprint.mission.discodeit.readstatus.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelPublicCreateRequestDTO;
import com.sprint.mission.discodeit.channel.web.dto.req.ChannelPrivateCreateRequestDTO;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.channel.web.dto.res.ChannelResponseDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChannelApplicationService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    public ChannelResponseDTO makePublicChannel(ChannelPublicCreateRequestDTO request) {
        Channel channel = Channel.init(
            request.name(),
            ChannelType.PUBLIC,
            request.description()
        );

        Channel saved = channelRepository.save(channel);

        return channelMapper.toResponse(saved, List.of(), null);
    }

    /*
        1. 채널저장
        2. 유저아이디 리스트로 들어온 것 + 채널 아이디 정보로 리드스테이터스 개체 일일이 생성
        3. 생성된 모든 개체 리드스테이터스 저장
     */
    @Transactional
    public ChannelResponseDTO makePrivateChannel(
        ChannelPrivateCreateRequestDTO request
    ) {
        Channel channel = Channel.init(null, ChannelType.PRIVATE, null);

        List<UUID> userIdList = request.participantIds();

        List<User> userList = userRepository.findAllById(userIdList);
        if(userIdList.size() != userList.size()) throw new CustomException(CustomErrorCode.USER_NOT_FOUND);

        Channel saved = channelRepository.save(channel);

        Instant lastReadAt = Instant.now();     // 채널 생성 시점 - 낫널
        List<ReadStatus> readStatuses = userList.stream()
            .map(user -> ReadStatus.init(user, saved, lastReadAt)
            )
            .toList();

        readStatusRepository.saveAll(readStatuses);


        return channelMapper.toResponse(saved, userList,null);
    }

    public void deleteChannel(UUID channelId){
        Channel channel = channelRepository.getByIdOrThrow(channelId);
        channelRepository.delete(channel);
        //messageService.deleteMessageByChannelId(channelId);
        //readStatusService.deleteReadStatusByChannelId(channelId);
        //channelService.deleteChannel(channelId);
    }

    /*
        입장 가능한 채널 목록
     */
    @Transactional
    public List<ChannelResponseDTO> findAllChannelByUserId(UUID userId){
        userRepository.validateExistsById(userId);

        List<Channel> accessibleChannels = channelRepository.
            findAccessibleChannelsByUserId(userId, ChannelType.PUBLIC);

        List<ChannelLastMessage> lastMessageByChannels = messageRepository.findLastMessageByChannels(accessibleChannels);

        List<Channel> privateChannels = accessibleChannels.stream()
            .filter(Channel::isPrivate)
            .toList();

        List<ReadStatus> readStatusList = readStatusRepository.findAllParticipantDetailByChannels(privateChannels);

        Map<UUID, Instant> lastMessagesMap = lastMessageByChannels.stream()
            .collect(Collectors.toMap(
                ChannelLastMessage::channelId,
                ChannelLastMessage::lastMessageAt
            ));

        Map<UUID, List<UserResponseDTO>> participantsByChannelId = toParticipantsByChannelId(readStatusList);

        return accessibleChannels.stream()
                .map(channel -> ChannelResponseDTO.of(
                    channel,
                    participantsByChannelId.getOrDefault(channel.getId(), List.of()),
                    lastMessagesMap.get(channel.getId())
                ))
                    .toList();
    }

    @Transactional
    public ChannelResponseDTO updateChannel(UUID channelId, ChannelUpdateRequestDTO request) {
        Channel channel = channelRepository.getByIdOrThrow(channelId);
        channel.updateChannelNameAndDescription(request.newName(), request.newDescription());

        return channelMapper.toResponse(channel, List.of(), null);
    }

    private Map<UUID, List<UserResponseDTO>> toParticipantsByChannelId(List<ReadStatus> readStatusList) {
        Map<UUID, List<UserResponseDTO>> participantsByChannelId = new HashMap<>();

        for (ReadStatus readStatus : readStatusList) {
            UUID channelId = readStatus.getChannel().getId();

            List<UserResponseDTO> participants =
                participantsByChannelId.get(channelId);

            if (Objects.isNull(participants)) {
                participants = new ArrayList<>();
                participantsByChannelId.put(channelId, participants);
            }

            participants.add(userMapper.toResponse(readStatus.getUser()));
        }

        return participantsByChannelId;
    }
}
