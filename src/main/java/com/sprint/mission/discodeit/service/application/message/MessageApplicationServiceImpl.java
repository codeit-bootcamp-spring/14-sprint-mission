package com.sprint.mission.discodeit.service.application.message;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.message.MessageDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class MessageApplicationServiceImpl implements MessageApplicationService {
    private final MessageDomainService messageDomainService;
    private final UserDomainService userDomainService;
    private final ChannelDomainService channelDomainService;
    private final BinaryContentDomainService binaryContentDomainService;
    private final ReadStatusDomainService readStatusDomainService;

    public MessageApplicationServiceImpl(
            MessageDomainService messageDomainService,
            UserDomainService userDomainService,
            ChannelDomainService channelDomainService,
            BinaryContentDomainService binaryContentDomainService,
            ReadStatusDomainService readStatusDomainService
    ) {
        this.messageDomainService = messageDomainService;
        this.userDomainService = userDomainService;
        this.channelDomainService = channelDomainService;
        this.binaryContentDomainService = binaryContentDomainService;
        this.readStatusDomainService = readStatusDomainService;
    }

    @Override
    public MessageResponseDto create(
            MessageCreateRequestDto messageCreateRequest,
            List<BinaryContentCreateRequestDto> attachmentRequests
    ) {
        UUID senderId = messageCreateRequest.getSenderId();
        UUID channelId = messageCreateRequest.getChannelId();

        log.info(
                "Message 생성 시작: senderId={}, channelId={}, attachmentCount={}",
                senderId,
                channelId,
                Objects.nonNull(attachmentRequests)
                        ? attachmentRequests.size()
                        : 0
        );

        validateMessageCreation(senderId, channelId);

        List<BinaryContent> createdAttachments = new ArrayList<>();
        Message createdMessage = null;

        try {
            List<UUID> attachmentIds = createAttachments(
                    attachmentRequests,
                    createdAttachments
            );

            if (Objects.nonNull(attachmentRequests)) {
                log.debug(
                        "Message 첨부파일 저장 완료: channelId={}, attachmentCount={}",
                        channelId,
                        attachmentIds.size()
                );
            }

            createdMessage = messageDomainService.create(Message.create(
                    messageCreateRequest.getContent(),
                    senderId,
                    channelId,
                    attachmentIds
            ));

            log.info(
                    "Message 생성 완료: messageId={}, senderId={}, channelId={}, attachmentCount={}",
                    createdMessage.getId(),
                    createdMessage.getSenderId(),
                    createdMessage.getChannelId(),
                    createdMessage.getAttachmentIds().size()
            );

            return MessageResponseDto.from(createdMessage);
        } catch (RuntimeException originalException) {
            rollbackMessageCreation(
                    createdMessage,
                    createdAttachments,
                    originalException
            );
            throw originalException;
        }
    }

    @Override
    public MessageResponseDto findById(UUID messageId) {
        log.debug(
                "Message 단건 조회: messageId={}",
                messageId
        );
        return MessageResponseDto.from(messageDomainService.findById(messageId));
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        channelDomainService.findById(channelId);

        List<MessageResponseDto> messageResponses = messageDomainService.findAllByChannelId(channelId)
                .stream()
                .map(MessageResponseDto::from)
                .toList();

        log.debug(
                "Channel Message 목록 조회 완료: channelId={}, count={}",
                channelId,
                messageResponses.size()
        );

        return messageResponses;
    }

    @Override
    public MessageResponseDto update(
            UUID messageId,
            MessageUpdateRequestDto messageUpdateRequest
    ) {
        log.info(
                "Message 수정 시작: messageId={}",
                messageId
        );

        Message message = messageDomainService.findById(messageId);
        Message updatingMessage = message.copy();
        updatingMessage.updateContent(messageUpdateRequest.getContent());

        Message updatedMessage = messageDomainService.update(updatingMessage);

        log.info(
                "Message 수정 완료: messageId={}",
                updatedMessage.getId()
        );

        return MessageResponseDto.from(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageDomainService.findById(messageId);
        List<BinaryContent> attachments = findAttachments(message.getAttachmentIds());

        log.info(
                "Message 삭제 시작: messageId={}, attachmentCount={}",
                messageId,
                attachments.size()
        );

        boolean messageDeleted = false;
        List<BinaryContent> deletedAttachments = new ArrayList<>();

        try {
            messageDomainService.delete(messageId);
            messageDeleted = true;

            for (BinaryContent attachment : attachments) {
                binaryContentDomainService.delete(attachment.getId());
                deletedAttachments.add(attachment);
            }

            log.info("Message 및 첨부파일 삭제 완료: messageId={}", messageId);
        } catch (RuntimeException originalException) {
            rollbackMessageDeletion(
                    message,
                    messageDeleted,
                    deletedAttachments,
                    originalException
            );
            throw originalException;
        }
    }

    private void validateMessageCreation(UUID senderId, UUID channelId) {
        userDomainService.findById(senderId);
        Channel channel = channelDomainService.findById(channelId);

        if (channel.getChannelType() != ChannelType.PRIVATE) {
            return;
        }

        boolean isParticipant =
                readStatusDomainService.existsByUserIdAndChannelId(senderId, channelId);

        if (isParticipant) {
            return;
        }

        log.warn(
                "PRIVATE Channel Message 생성 거부: userId={}, channelId={}",
                senderId,
                channelId
        );

        throw new CustomException(
                ExceptionType.CHANNEL_ACCESS_DENIED,
                senderId,
                channelId
        );
    }

    private List<UUID> createAttachments(
            List<BinaryContentCreateRequestDto> attachmentRequests,
            List<BinaryContent> createdAttachments
    ) {
        List<UUID> attachmentIds = new ArrayList<>();
        if (Objects.isNull(attachmentRequests)) {
            return attachmentIds;
        }

        for (BinaryContentCreateRequestDto attachmentRequest : attachmentRequests) {
            BinaryContent attachment = BinaryContent.create(
                    attachmentRequest.getFileName(),
                    attachmentRequest.getBytes()
            );
            BinaryContent createdAttachment =
                    binaryContentDomainService.create(attachment);

            createdAttachments.add(createdAttachment);
            attachmentIds.add(createdAttachment.getId());
        }

        return attachmentIds;
    }

    private List<BinaryContent> findAttachments(List<UUID> attachmentIds) {
        List<BinaryContent> attachments = new ArrayList<>();

        for (UUID attachmentId : attachmentIds) {
            attachments.add(binaryContentDomainService.findById(attachmentId));
        }

        return attachments;
    }

    private void rollbackMessageCreation(
            Message createdMessage,
            List<BinaryContent> createdAttachments,
            RuntimeException originalException
    ) {
        if (Objects.nonNull(createdMessage)) {
            try {
                messageDomainService.delete(createdMessage.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }

        for (int index = createdAttachments.size() - 1; index >= 0; index--) {
            BinaryContent createdAttachment = createdAttachments.get(index);
            try {
                binaryContentDomainService.delete(createdAttachment.getId());
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }

    private void rollbackMessageDeletion(
            Message message,
            boolean messageDeleted,
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

        if (messageDeleted) {
            try {
                messageDomainService.create(message);
            } catch (RuntimeException rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
