package com.sprint.mission.discodeit.readStatus.application.basic;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.readStatus.dto.*;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateReadStatusException;
import com.sprint.mission.discodeit.common.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readStatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.readStatus.application.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
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
            log.warn("읽음 상태 생성 실패 - 존재하지 않는 채널: channelId = {}", request.channelId());
            throw new ChannelNotFoundException(request.channelId());
        }
        // 유저 없으면 예외
        if (userRepository.findById(request.userId()).isEmpty()) {
            log.warn("읽음 상태 생성 실패 - 존재하지 않는 사용자: userId = {}", request.userId());
            throw new UserNotFoundException(request.userId());
        }
        // 채널명, 유저명이 이미 있으면 예외
        if (readStatusRepository.existsByChannelIdAndUserId(request.channelId(), request.userId())) {
            log.warn("읽음 상태 생성 실패 - 이미 존재함: userId = {}, channelId = {}", request.userId(), request.channelId());
            throw new DuplicateReadStatusException(request.userId(), request.channelId());
        }

        User user = userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(request.userId()));
        Channel channel = channelRepository.findById(request.channelId()).orElseThrow(() -> new ChannelNotFoundException(request.channelId()));

        ReadStatus readStatus = ReadStatus.create(user, channel, request.lastReadAt());
        readStatusRepository.save(readStatus);

        log.info("읽음 상태 생성 성공 - readStatusId = {}, userId = {}, channelId = {}",
                readStatus.getId(), request.userId(), request.channelId());
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusDto find(UUID id) {
        ReadStatus readStatus = check(id);

        log.debug("읽음 상태 조회 성공 - readStatusId = {}", id);
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatusDto> readStatuses = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .toList();
        log.debug("사용자별 읽음 상태 목록 조회 성공 - userId = {}, count = {}", userId, readStatuses.size());
        return readStatuses;
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequestDto request) {

        ReadStatus readStatus = check(id);
        readStatus.updateTime(request.newLastReadAt());
//        readStatusRepository.update(readStatus); 변경 감지

        // 메시지를 읽을 때마다 호출되는 잦은 갱신이라 debug
        log.debug("읽음 상태 수정 성공 - readStatusId = {}", id);
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        check(id); // 없으면 예외 띄우기용도
        readStatusRepository.deleteById(id);
        log.info("읽음 상태 삭제 성공 - readStatusId = {}", id);
    }

    private ReadStatus check(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(() -> {
            log.warn("읽음 상태 찾기 실패 - 존재하지 않는 읽음 상태: readStatusId = {}", id);
            return new ReadStatusNotFoundException(id);
        });
    }

}
