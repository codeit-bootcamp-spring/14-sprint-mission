package com.sprint.mission.discodeit.service.application.channel;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.message.MessageDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChannelApplicationServiceImpl implements ChannelApplicationService {
    private final ChannelDomainService channelDomainService;
    private final ReadStatusDomainService readStatusDomainService;
    private final MessageDomainService messageDomainService;
    private final UserDomainService userDomainService;
    private final BinaryContentDomainService binaryContentDomainService;

    public ChannelApplicationServiceImpl(
            ChannelDomainService channelDomainService,
            ReadStatusDomainService readStatusDomainService,
            MessageDomainService messageDomainService,
            UserDomainService userDomainService,
            BinaryContentDomainService binaryContentDomainService
    ) {
        this.channelDomainService = channelDomainService;
        this.readStatusDomainService = readStatusDomainService;
        this.messageDomainService = messageDomainService;
        this.userDomainService = userDomainService;
        this.binaryContentDomainService = binaryContentDomainService;
    }

    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        Channel createdChannel = null;

        try {
            createdChannel = channelDomainService.create(
                    Channel.createPublic(
                            request.getName(),
                            request.getDescription()
                    )
            );

            createReadStatusesForAllUsers(createdChannel.getId());

            return ChannelResponseDto.from(
                    createdChannel,
                    null,
                    List.of()
            );
        } catch (RuntimeException originalException) {
            rollbackCreatedChannel(createdChannel, originalException);
            throw originalException;
        }
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto privateChannelCreateRequest) {
        List<UUID> participantUserIds =
                getValidatedParticipantIds(privateChannelCreateRequest);

        log.info(
                "PRIVATE Channel 생성 시작: participantCount={}",
                participantUserIds.size()
        );

        Channel createdChannel = null;
        List<ReadStatus> createdReadStatuses = new ArrayList<>();

        try {
            Channel privateChannel = Channel.createPrivate();
            createdChannel = channelDomainService.create(privateChannel);

            // participating 유저 당 read status 만들기
            for (UUID participantUserId : participantUserIds) {
                ReadStatus readStatus = ReadStatus.create(
                        participantUserId,
                        createdChannel.getId()
                );
                ReadStatus createdReadStatus =
                        readStatusDomainService.create(readStatus);
                createdReadStatuses.add(createdReadStatus);
            }

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
        } catch (RuntimeException originalException) {
            rollbackPrivateChannelCreation(
                    createdChannel,
                    createdReadStatuses,
                    originalException
            );
            throw originalException;
        }
    }

    @Override
    public ChannelResponseDto findById(UUID channelId) {
        log.debug(
                "Channel 단건 조회: channelId={}",
                channelId
        );

        Channel channel = channelDomainService.findById(channelId);
        Instant mostRecentMessageAt = findMostRecentMessageAt(channelId);
        List<UUID> participantUserIds = findParticipantUserIds(channel);

        return ChannelResponseDto.from(
                channel,
                mostRecentMessageAt,
                participantUserIds
        );
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        userDomainService.findById(userId);

        List<ChannelResponseDto> channelResponses = new ArrayList<>();

        Set<UUID> accessiblePrivateChannelIds = readStatusDomainService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        for (Channel channel : channelDomainService.findAll()) {
            if (!isAccessible(channel, accessiblePrivateChannelIds)) {
                continue;
            }

            channelResponses.add(findById(channel.getId()));
        }

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
            ChannelUpdateRequestDto channelUpdateRequest
    ) {
        log.info("Channel 수정 시작: channelId={}", channelId);

        Channel channel = channelDomainService.findById(channelId);
        Instant mostRecentMessageAt = findMostRecentMessageAt(channelId);

        Channel updatingChannel = channel.copy();
        updatingChannel.updateNameAndDescription(
                channelUpdateRequest.getName(),
                channelUpdateRequest.getDescription()
        );

        Channel updatedChannel = channelDomainService.update(updatingChannel);

        log.info("Channel 수정 완료: channelId={}", updatedChannel.getId());

        return ChannelResponseDto.from(
                updatedChannel,
                mostRecentMessageAt,
                List.of()
        );
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelDomainService.findById(channelId);
        List<Message> messages = messageDomainService.findAllByChannelId(channelId);
        List<ReadStatus> readStatuses = readStatusDomainService.findAllByChannelId(channelId);
        List<BinaryContent> attachments = findAttachments(messages);

        log.info(
                "Channel 삭제 시작: channelId={}, messageCount={}, attachmentCount={}",
                channelId,
                messages.size(),
                attachments.size()
        );

        List<ReadStatus> deletedReadStatuses = new ArrayList<>();
        List<Message> deletedMessages = new ArrayList<>();
        List<BinaryContent> deletedAttachments = new ArrayList<>();
        boolean channelDeleted = false;

        try {
            for (ReadStatus readStatus : readStatuses) {
                readStatusDomainService.delete(readStatus.getId());
                deletedReadStatuses.add(readStatus);
            }

            for (Message message : messages) {
                messageDomainService.delete(message.getId());
                deletedMessages.add(message);
            }

            channelDomainService.delete(channelId);
            channelDeleted = true;

            for (BinaryContent attachment : attachments) {
                binaryContentDomainService.delete(attachment.getId());
                deletedAttachments.add(attachment);
            }

            log.info("Channel 및 연관 데이터 삭제 완료: channelId={}", channelId);
        } catch (RuntimeException originalException) {
            rollbackChannelDeletion(
                    channel,
                    channelDeleted,
                    deletedReadStatuses,
                    deletedMessages,
                    deletedAttachments,
                    originalException
            );
            throw originalException;
        }
    }

    private void createReadStatusesForAllUsers(UUID channelId) {
        List<ReadStatus> readStatuses = userDomainService.findAll()
                .stream()
                .map(user -> ReadStatus.create(user.getId(), channelId))
                .toList();

        readStatusDomainService.createAll(readStatuses);
    }

    private List<UUID> getValidatedParticipantIds(
            PrivateChannelCreateRequestDto request
    ) {
        List<UUID> participantUserIds = request.getParticipantUserIds()
                .stream()
                .distinct()
                .toList();

        participantUserIds.forEach(userDomainService::findById);
        return participantUserIds;
    }

    private Instant findMostRecentMessageAt(UUID channelId) {
        Message mostRecentMessage =
                messageDomainService.findMostRecentByChannelId(channelId);

        return Objects.nonNull(mostRecentMessage)
                ? mostRecentMessage.getCreatedAt()
                : null;
    }

    private List<UUID> findParticipantUserIds(Channel channel) {
        if (channel.getChannelType() != ChannelType.PRIVATE) {
            return List.of();
        }

        return readStatusDomainService.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();
    }

    private boolean isAccessible(
            Channel channel,
            Set<UUID> accessiblePrivateChannelIds
    ) {
        return channel.getChannelType() == ChannelType.PUBLIC
                || accessiblePrivateChannelIds.contains(channel.getId());
    }

    private List<BinaryContent> findAttachments(List<Message> messages) {
        Set<UUID> attachmentIds = new LinkedHashSet<>();

        for (Message message : messages) {
            attachmentIds.addAll(message.getAttachmentIds());
        }

        return attachmentIds.stream()
                .map(binaryContentDomainService::findById)
                .toList();
    }

    private void rollbackCreatedChannel(
            Channel createdChannel,
            RuntimeException originalException
    ) {
        if (Objects.isNull(createdChannel)) {
            return;
        }

        try {
            channelDomainService.delete(createdChannel.getId());
        } catch (RuntimeException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }

    private void rollbackPrivateChannelCreation(
            Channel createdChannel,
            List<ReadStatus> createdReadStatuses,
            RuntimeException originalException
    ) {
        for (int index = createdReadStatuses.size() - 1; index >= 0; index--) {
            ReadStatus createdReadStatus = createdReadStatuses.get(index);
            try {
                readStatusDomainService.delete(createdReadStatus.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        rollbackCreatedChannel(createdChannel, originalException);
    }

    private void rollbackChannelDeletion(
            Channel channel,
            boolean channelDeleted,
            List<ReadStatus> deletedReadStatuses,
            List<Message> deletedMessages,
            List<BinaryContent> deletedAttachments,
            RuntimeException originalException
    ) {
        for (int index = deletedAttachments.size() - 1; index >= 0; index--) {
            BinaryContent deletedAttachment = deletedAttachments.get(index);
            try {
                binaryContentDomainService.create(deletedAttachment);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        if (channelDeleted) {
            try {
                channelDomainService.create(channel);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        for (int index = deletedMessages.size() - 1; index >= 0; index--) {
            Message deletedMessage = deletedMessages.get(index);
            try {
                messageDomainService.create(deletedMessage);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        for (int index = deletedReadStatuses.size() - 1; index >= 0; index--) {
            ReadStatus deletedReadStatus = deletedReadStatuses.get(index);
            try {
                readStatusDomainService.create(deletedReadStatus);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
