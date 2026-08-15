package com.sprint.mission.discodeit.service.application.channel;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.message.MessageDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelApplicationServiceImpl implements ChannelApplicationService {

    private final ChannelDomainService channelDomainService;
    private final ReadStatusDomainService readStatusDomainService;
    private final MessageDomainService messageDomainService;
    private final UserDomainService userDomainService;
    private final BinaryContentDomainService binaryContentDomainService;


    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        Channel createdChannel = channelDomainService.create(
                Channel.createPublic(
                        request.getName(),
                        request.getDescription()
                )
        );

        userDomainService.findAll()
                .forEach(user -> {
                            ReadStatus readStatus = ReadStatus.create(user.getId(), createdChannel.getId());
                            readStatusDomainService.create(readStatus);
                        }
                );

        return ChannelResponseDto.from(
                createdChannel,
                null,
                List.of()
        );
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request) {
        List<UUID> participantUserIds = request.getParticipantUserIds()
                .stream()
                .distinct()
                .toList();

        participantUserIds.forEach(userDomainService::findById);

        log.info("PRIVATE Channel 생성 시작: participantCount={}", participantUserIds.size());
        Channel createdChannel = channelDomainService.create(Channel.createPrivate());

        participantUserIds.forEach((userId) -> {
            ReadStatus readStatus = ReadStatus.create(
                    userId,
                    createdChannel.getId()
            );
            readStatusDomainService.create(readStatus);
        });

        log.debug(
                "PRIVATE Channel ReadStatus 생성 완료: channelId={}, participantCount={}",
                createdChannel.getId(),
                participantUserIds.size()
        );

        return ChannelResponseDto.from(
                createdChannel,
                null,
                participantUserIds
        );
    }

    @Override
    public ChannelResponseDto findById(UUID channelId) {
        log.debug("Channel 단건 조회: channelId={}", channelId);

        Channel channel = channelDomainService.findById(channelId);

        Message mostRecentMessage = messageDomainService.findMostRecentByChannelId(channelId);
        Instant mostRecentMessageAt = Objects.nonNull(mostRecentMessage)
                ? mostRecentMessage.getCreatedAt()
                : null;

        List<UUID> participantUserIds = (channel.getChannelType() == ChannelType.PRIVATE)
                ? readStatusDomainService.findAllByChannelId(channelId)
                    .stream()
                    .map(ReadStatus::getUserId)
                    .toList()
                : List.of();

        return ChannelResponseDto.from(
                channel,
                mostRecentMessageAt,
                participantUserIds
        );
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        userDomainService.findById(userId);

        Set<UUID> accessiblePrivateChannelIds = readStatusDomainService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        List<ChannelResponseDto> channelResponses = channelDomainService.findAll().stream()
                .filter(channel -> isAccessible(channel, accessiblePrivateChannelIds))
                .map(channel -> this.findById(channel.getId()))
                .toList();

        log.debug(
                "User가 볼 수 있는 Channel 목록 조회 완료: userId={}, count={}",
                userId,
                channelResponses.size()
        );

        return channelResponses;
    }

    // 주의: PUBLIC 채널의 name과 description만 바꿀 수 있다.
    @Override
    public ChannelResponseDto update(
            UUID channelId,
            ChannelUpdateRequestDto request
    ) {
        log.info("Channel 수정 시작: channelId={}", channelId);

        Channel updatingChannel = channelDomainService.findById(channelId);
        if (updatingChannel.getChannelType().equals(ChannelType.PRIVATE)) {
            log.warn("비공개 Channel 수정 불가: channelId={}", channelId);
            throw new CustomException(ExceptionType.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED, channelId);
        }

        updatingChannel.updateNameAndDescription(request.getName(), request.getDescription());
        Channel updatedChannel = channelDomainService.update(updatingChannel);

        log.info("Channel 수정 완료: channelId={}", updatedChannel.getId());

        Message mostRecentMessage = messageDomainService.findMostRecentByChannelId(channelId);
        Instant mostRecentMessageAt = Objects.nonNull(mostRecentMessage)
                ? mostRecentMessage.getCreatedAt()
                : null;

        return ChannelResponseDto.from(
                updatedChannel,
                mostRecentMessageAt,
                List.of()
        );
    }

    @Override
    public void delete(UUID channelId) {
        channelDomainService.findById(channelId);
        List<UUID> messageIds =
                messageDomainService.findAllByChannelId(channelId)
                        .stream()
                        .map(Message::getId)
                        .toList();
        List<UUID> readStatusIds =
                readStatusDomainService.findAllByChannelId(channelId)
                        .stream()
                        .map(ReadStatus::getId)
                        .toList();

        List<UUID> attachmentIds = messageIds.stream()
                .filter(messageId ->
                        Objects.nonNull(
                                messageDomainService.findById(messageId)
                                        .getAttachmentIds())
                )
                .flatMap(messageId ->
                        messageDomainService.findById(messageId)
                                .getAttachmentIds()
                                .stream()
                )
                .map(binaryContentDomainService::findById)
                .map(BinaryContent::getId)
                .toList();

        log.info(
                "Channel 삭제 시작: channelId={}, messageCount={}, attachmentCount={}",
                channelId,
                messageIds.size(),
                attachmentIds.size()
        );

        readStatusIds.forEach(readStatusDomainService::delete);
        messageIds.forEach(messageDomainService::delete);
        attachmentIds.forEach(binaryContentDomainService::delete);
        channelDomainService.delete(channelId);

        log.info("Channel 및 연관 데이터 삭제 완료: channelId={}", channelId);
    }

    private boolean isAccessible(
            Channel channel,
            Set<UUID> accessiblePrivateChannelIds
    ) {
        return channel.getChannelType() == ChannelType.PUBLIC
                || accessiblePrivateChannelIds.contains(channel.getId());
    }
}
