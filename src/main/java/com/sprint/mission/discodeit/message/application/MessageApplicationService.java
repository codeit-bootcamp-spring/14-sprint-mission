package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.binarycontent.application.BinaryApplicationService;
import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.channel.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.pagination.PageResponse;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.domain.entity.Message;
import com.sprint.mission.discodeit.message.domain.repository.MessageRepository;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.message.web.dto.req.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.message.web.dto.res.MessageResponseDTO;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MessageApplicationService {
    private final BinaryApplicationService binaryApplicationService;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public MessageResponseDTO createMessage(MessageCreateRequestDTO request, List<MultipartFile> files){
        User user = userRepository.getByIdOrThrow(request.authorId());
        Channel channel = channelRepository.getByIdOrThrow(request.channelId());
        Message message = Message.init(user, channel, request.content());

        if(Objects.nonNull(files) && !files.isEmpty()){
            List<BinaryContent> attachments = binaryApplicationService.storeMultipartFiles(files);
            message.updateMessageImagesField(attachments);
        }

        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Transactional
    public MessageResponseDTO updateMessageContent(UUID messageId, String content) {
        Message message = messageRepository.getByIdOrThrow(messageId);
        message.updateContent(content);

        return messageMapper.toResponse(message);
    }

    @Transactional
    public void deleteMessage(UUID messageId){
        Message message = messageRepository.getByIdOrThrow(messageId);
        messageRepository.delete(message);

//        if(message.hasImageList()){
//            message.getImageList()                todo : 바이너라파일 관계
//                .forEach(binaryContentService::deleteStoreFileById);
//        }
    }

    @Transactional
    public PageResponse<MessageResponseDTO> findAllMessageByChannelId(UUID channelId, Pageable pageable) {

        Slice<MessageResponseDTO> responseSlice = messageRepository.findAllByChannelId(channelId, pageable)
            .map(messageMapper::toResponse);

        return PageResponse.fromSlice(responseSlice);
    }
}
