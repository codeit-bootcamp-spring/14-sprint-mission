package com.sprint.mission.discodeit.message.application.basic;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.common.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.message.MessageMapper;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.message.application.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;


    @Override
    @Transactional
    public MessageDto create(MessageCreateRequestDto request, List<MultipartFile> attachments) {

        Channel channel = channelRepository.findById(request.channelId()).orElseThrow(NoSuchElementException::new);
        User author = userRepository.findById(request.authorId()).orElseThrow(NoSuchElementException::new);

        List<BinaryContent> attachmentList = new ArrayList<>();

        if (attachments != null) {
            for (MultipartFile file : attachments) {

                BinaryContent binaryContent = new BinaryContent(file.getOriginalFilename(), file.getSize(),
                        file.getContentType());
//                    binaryContentRepository.save(binaryContent);  // DB에 CasCade 걸어놨는데 잘 작동하는지 확인
                attachmentList.add(binaryContent);
            }
        }

        Message message = Message.create(attachmentList, request.content(), channel, author);
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID id) {
        Message message = check(id);
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public MessageDto update(UUID id, MessageUpdateRequestDto request) {
        Message message = check(id);
        message.update(request.newMessage());
//        messageRepository.save(message);  // 변경 감지

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Message message = check(id);

        List<BinaryContent> attachments = message.getAttachments();

//        for (BinaryContent attachment : attachments) {
//            binaryContentRepository.deleteById(attachment.getId());
//        }
        // 안해도 삭제됨


        messageRepository.deleteById(id);


    }

    @Override
    @Transactional
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        Slice<MessageDto> slice = messageRepository.findAllByChannelId(channelId, pageable)
                .map(messageMapper::toDto);

        return pageResponseMapper.fromSlice(slice);
    }


    private Message check(UUID id) {

        return messageRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
