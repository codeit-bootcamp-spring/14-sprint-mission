package com.sprint.mission.discodeit.service.application.message;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.message.MessageDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageApplicationServiceImpl implements MessageApplicationService {

    private final MessageDomainService messageDomainService;
    private final UserDomainService userDomainService;
    private final ChannelDomainService channelDomainService;
    private final BinaryContentDomainService binaryContentDomainService;
    private final ReadStatusDomainService readStatusDomainService;

    private boolean isAccessible(
            Channel channel,
            Set<UUID> accessibleChannelIds
    ) {
        return channel.getChannelType() == ChannelType.PUBLIC
                || accessibleChannelIds.contains(channel.getId());
    }

    private List<UUID> createAttachments(
            List<MultipartFile> attachmentFiles
    ) {
        if (Objects.isNull(attachmentFiles) || attachmentFiles.isEmpty()) {
            return List.of();
        }

        return attachmentFiles.stream()
                .filter(Objects::nonNull)
                .filter(file -> !file.isEmpty())
                .map((multipartFile) -> {
                    return binaryContentDomainService.create(
                            BinaryContent.create(multipartFile)
                    );
                })
                .map(BinaryContent::getId)
                .toList();
    }


    @Override
    public MessageResponseDto create(
            MessageCreateRequestDto messageCreateRequest,
            List<MultipartFile> attachments
    ) {
        int numAttachments = (Objects.isNull(attachments))
                ? 0
                : attachments.size();

        log.info(
                "Message 생성 시작: senderId={}, channelId={}, attachmentCount={}",
                messageCreateRequest.getSenderId(),
                messageCreateRequest.getChannelId(),
                numAttachments
        );

        UUID senderId = userDomainService.findById(messageCreateRequest.getSenderId()).getId();
        UUID channelId = channelDomainService.findById(messageCreateRequest.getChannelId()).getId();
        boolean senderCanAccess = readStatusDomainService.existsByUserIdAndChannelId(senderId, channelId);

        if (!senderCanAccess) {
            log.warn(
                    "PRIVATE Channel Message 생성 거부: userId={}, channelId={}",
                    senderId,
                    channelId
            );
            throw new DiscodeitException(
                    ExceptionType.CHANNEL_ACCESS_DENIED,
                    senderId,
                    channelId
            );
        }

        List<UUID> attachmentIds = createAttachments(attachments);

        if (Objects.nonNull(attachmentIds)) {
            log.info(
                    "Message 첨부파일 저장 완료: channelId={}, attachmentCount={}",
                    channelId,
                    attachmentIds.size()
            );
        }

        Message createdMessage = messageDomainService.create(Message.create(
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
    }

    @Override
    public MessageResponseDto findById(UUID messageId) {
        log.debug("Message 단건 조회: messageId={}", messageId);
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

        Message updatingMessage = messageDomainService.findById(messageId);
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
        List<UUID> attachmentIds = messageDomainService.findById(messageId).getAttachmentIds();

        log.info(
                "Message 삭제 시작: messageId={}, attachmentCount={}",
                message.getId(),
                attachmentIds.size()
        );

        messageDomainService.delete(messageId);

        for (UUID attachmentId : attachmentIds) {
            binaryContentDomainService.delete(attachmentId);
        }

        log.info("Message 및 첨부파일 삭제 완료: messageId={}", messageId);
    }
}
