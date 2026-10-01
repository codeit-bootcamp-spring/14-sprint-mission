package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentUploadRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    public MessageDto createMessage(UUID channelId, UUID authorId, String content,
        List<BinaryContentUploadRequest> attachmentRequests) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        User user = userRepository.findById(authorId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Message message = Message.create(content, channel, user);

        if (attachmentRequests != null && !attachmentRequests.isEmpty()) {
            for (BinaryContentUploadRequest request : attachmentRequests) {
                BinaryContent attachment = BinaryContent.create(
                    request.fileName(),
                    request.size(),
                    request.contentType()
                );
                BinaryContent savedAttachment = binaryContentRepository.save(attachment);
                binaryContentStorage.put(savedAttachment.getId(), request.bytes());

                message.addAttachment(savedAttachment);
            }
        }

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    @Transactional
    public MessageDto updateMessageContent(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("메세지를 찾을 수 없습니다."));

        message.updateContent(newContent);
        return messageMapper.toDto(message);
    }

    public PageResponse<MessageDto> getMessagesByChannel(UUID channelId, int page) {
        Pageable pageable = PageRequest.of(page, 50, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<Message> messageSlice = messageRepository.findAllByChannelId(channelId, pageable);
        Slice<MessageDto> messageDtoSlice = messageSlice.map(messageMapper::toDto);
        return pageResponseMapper.fromSlice(messageDtoSlice);
    }
}
