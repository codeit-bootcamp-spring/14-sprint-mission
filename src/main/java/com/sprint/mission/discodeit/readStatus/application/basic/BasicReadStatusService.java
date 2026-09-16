package com.sprint.mission.discodeit.readStatus.application.basic;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.readStatus.dto.*;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.common.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.common.exception.NotFoundUserException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readStatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.readStatus.application.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusDto create(ReadStatusCreateRequestDto request) {

        // 채널 없으면 예외
        if (channelRepository.findById(request.channelId()).isEmpty()) {
            throw new NotFoundChannelException();
        }
        // 유저 없으면 예외
        if (userRepository.findById(request.userId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        // 채널명, 유저명이 이미 있으면 예외
        if (readStatusRepository.existsByChannelIdAndUserId(request.channelId(), request.userId())) {
            throw new DuplicateStatus();
        }

        User user = userRepository.findById(request.userId()).orElseThrow(NotFoundUserException::new);
        Channel channel = channelRepository.findById(request.channelId()).orElseThrow(NotFoundChannelException::new);

        ReadStatus readStatus = ReadStatus.create(user, channel);
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusDto find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequestDto request) {

        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(NoSuchElementException::new);
        readStatus.updateTime(request.newLastReadAt());
//        readStatusRepository.update(readStatus); 변경 감지

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        readStatusRepository.findById(id).orElseThrow(NoSuchElementException::new); // 없으면 예외 띄우기용도
        readStatusRepository.deleteById(id);
    }

}
