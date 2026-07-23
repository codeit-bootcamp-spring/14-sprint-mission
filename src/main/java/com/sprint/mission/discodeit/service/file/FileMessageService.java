package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final FileMessageRepository messageRepository;

    public FileMessageService() {
        this.messageRepository = new FileMessageRepository();
    }

    @Override
    public MessageResponseDto create(MessageCreateRequestDto requestDto) {
        Message newMessage = Message.from(requestDto);
        messageRepository.save(newMessage);

        return MessageResponseDto.from(newMessage);
    }

    @Override
    public MessageResponseDto read(UUID id) {
        return MessageResponseDto.from(
                messageRepository.find(id)
        );
    }

    @Override
    public List<MessageResponseDto> readAll() {
        return messageRepository.findAll().stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    @Override
    public MessageResponseDto update(MessageUpdateRequestDto requestDto) {
        Message messageToUpdate = messageRepository.find(requestDto.getId());
        messageToUpdate.update(requestDto.getContent());
        messageRepository.save(messageToUpdate);
        return MessageResponseDto.from(messageToUpdate);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
