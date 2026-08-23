package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    public ReadStatus create(ReadStatus readStatus) {
        return readStatusRepository.create(readStatus);
    }

    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        return readStatusRepository.createAll(readStatuses);
    }

    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.READSTATUS_NOT_FOUND_IN_DATABASE));
    }


    public List<UUID> findAllUserIdsByChannelId(UUID channelId) {
        return readStatusRepository.findAllUserIdsByChannelId(channelId);
    }

    public boolean existsByUserAndChannel(UUID userId, UUID channelId) {
        return readStatusRepository.existsByUserAndChannel(userId, channelId);
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public ReadStatus update(UUID id, Instant newLastReadAt) {
        validateExists(id);
        return readStatusRepository.update(id, newLastReadAt);
    }

    public ReadStatus deleteById(UUID id) {
        return readStatusRepository.deleteById(id);
    }

    public void deleteByUserId(UUID userId) {
        readStatusRepository.deleteByUserId(userId);
    }

    public void deleteByChannelId(UUID channelId) {
        readStatusRepository.deleteByChannelId(channelId);
    }

    public void validateExists(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new CustomException(ExceptionType.READSTATUS_NOT_FOUND_IN_DATABASE);
        }
    }

    public void validateAlreadyExistsByUserAndChannel(UUID userId, UUID channelID) {
        if (readStatusRepository.existsByUserAndChannel(userId, channelID)) {
            throw new CustomException(ExceptionType.READSTATUS_ALREADY_EXISTS);
        }
    }
}