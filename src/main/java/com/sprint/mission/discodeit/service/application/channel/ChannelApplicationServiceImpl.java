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
            Channel savedChannel = channelDomainService.create(
                    Channel.createPublic(
                            request.getName(),
                            request.getDescription()
                    )
            );
            createdChannel = savedChannel;

            List<ReadStatus> readStatuses =
                    userDomainService.findAll()
                            .stream()
                            .map(user -> ReadStatus.create(
                                    user.getId(),
                                    savedChannel.getId()
                            ))
                            .toList();

            readStatusDomainService.createAll(readStatuses);

            return ChannelResponseDto.from(
                    createdChannel,
                    null,
                    List.of()
            );
        } catch (RuntimeException originalException) {
            if (Objects.nonNull(createdChannel)) {
                try {
                    channelDomainService.delete(createdChannel.getId());
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }
            throw originalException;
        }
    }


    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto privateChannelCreateRequest) {

        // 1. 존재하는 user id인지 확인
        // 2. 중복되는 participant 없는지 확인 - 중복된 게 있으면 read status가 중복 생성 된다
        List<UUID> participantUserIds = privateChannelCreateRequest
                .getParticipantUserIds()
                .stream()
                .distinct()
                .toList();

        log.info(
                "PRIVATE Channel 생성 시작: participantCount={}",
                privateChannelCreateRequest.getParticipantUserIds().size()
        );

        for (UUID participantUserId : participantUserIds) {
            userDomainService.findById(participantUserId);
        }

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
            for (int index = createdReadStatuses.size() - 1; index >= 0; index--) {
                try {
                    readStatusDomainService.delete(
                            createdReadStatuses.get(index).getId()
                    );
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            if (Objects.nonNull(createdChannel)) {
                try {
                    channelDomainService.delete(createdChannel.getId());
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            throw originalException;
        }
    }


    @Override
    public ChannelResponseDto findById(UUID channelId) {
        log.debug(
                "Channel 단건 조회: channelId={}",
                channelId
        );

        // 찾으려는 channel
        Channel channel = channelDomainService.findById(channelId);

        // 그 채널에 보내진 가장 마지막 메세지의 전송 시각이 뭔지
        Message mostRecentMessage = messageDomainService.findMostRecentByChannelId(channelId);
        Instant mostRecentMessageAt = (Objects.nonNull(mostRecentMessage))
                ? mostRecentMessage.getCreatedAt()  // 보낸 메시지가 있을 경우
                : null;     // 아직 아무 메시지도 안 보냈을 경우


        // (private 채널이면,) 그 채널의 참여 user가 누구누구인지
        List<UUID> participantUserIds = (channel.getChannelType() == ChannelType.PRIVATE)
                ? readStatusDomainService
                    .findAllByChannelId(channelId)
                    .stream()
                    .map(ReadStatus::getUserId) // 어플리케이션 레벨
                    .toList()
                : List.of();

        // Dto로 반환
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

        // userId가 접근 가능한 모든 private channel들
        Set<UUID> accessiblePrivateChannelIds = readStatusDomainService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        // 모든 채널에서,
        for (Channel channel : channelDomainService.findAll()) {
            // 접근할 수 없는 private channel 제외하고, 모든 채널에 대해
            if (channel.getChannelType() == ChannelType.PRIVATE
                    && !accessiblePrivateChannelIds.contains(channel.getId())) {
                continue;
            }

            // 채널 찾기
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

        // 수정 이후의 조회 실패 때문에 이미 변경된 Channel이 남지 않도록
        // DTO 구성에 필요한 조회를 상태 변경 전에 끝낸다.
        channelDomainService.findById(channelId);
        Message mostRecentMessage = messageDomainService.findMostRecentByChannelId(channelId);
        Instant mostRecentMessageAt = (Objects.nonNull(mostRecentMessage))
                ? mostRecentMessage.getCreatedAt()
                : null;

        // update할 정보가 담겨 있는 채널 객체 생성
        Channel channelUpdates = Channel.createPublic(
                channelUpdateRequest.getName(),
                channelUpdateRequest.getDescription()
        );

        // 그 객체를 channelDomainService의 업데이트에 채널 id와 보냄
        Channel updatedChannel = channelDomainService.update(
                channelId,
                channelUpdates
        );

        log.info("Channel 수정 완료: channelId={}", updatedChannel.getId());

        // Dto 반환
        return ChannelResponseDto.from(
                updatedChannel,
                mostRecentMessageAt,
                List.of()   // update()는 public 채널만 할 수 있음
        );
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelDomainService.findById(channelId);

        // 채널로 보낸 모든 메시지 리스트
        List<Message> messages =
                messageDomainService.findAllByChannelId(channelId);

        List<ReadStatus> readStatuses =
                readStatusDomainService.findAllByChannelId(channelId);

        // 그 메시지들의 attachment id 리스트
        List<UUID> attachmentIds = new ArrayList<>();
        for (Message message : messages) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                if (!attachmentIds.contains(attachmentId)) {
                    attachmentIds.add(attachmentId);
                }
            }
        }

        List<BinaryContent> attachments = new ArrayList<>();
        for (UUID attachmentId : attachmentIds) {
            attachments.add(binaryContentDomainService.findById(attachmentId));
        }

        log.info(
                "Channel 삭제 시작: channelId={}, messageCount={}, attachmentCount={}",
                channelId,
                messages.size(),
                attachmentIds.size()
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

            // attachment를 참조하는 Message를 먼저 제거한다.
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
            for (int index = deletedAttachments.size() - 1; index >= 0; index--) {
                try {
                    binaryContentDomainService.create(
                            deletedAttachments.get(index)
                    );
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
                try {
                    messageDomainService.create(deletedMessages.get(index));
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            for (int index = deletedReadStatuses.size() - 1; index >= 0; index--) {
                try {
                    readStatusDomainService.create(
                            deletedReadStatuses.get(index)
                    );
                } catch (RuntimeException rollbackException) {
                    originalException.addSuppressed(rollbackException);
                }
            }

            throw originalException;
        }
    }
}
