package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.readstatus.ReadStatusRepository;
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
        validateUserAndChannelAvailable(readStatus);
        return readStatusRepository.save(readStatus);
    }

    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        validateUserAndChannelAvailable(readStatuses);
        return readStatusRepository.saveAll(readStatuses);
    }

    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.READSTATUS_NOT_FOUND_IN_DATABASE));
    }


    public List<UUID> findAllUserIdsByChannelId(UUID channelId) {
        return readStatusRepository.findAllUserIdByChannelId(channelId);
    }

    public boolean existsByUserAndChannel(UUID userId, UUID channelId) {
        return readStatusRepository.existsByUser_IdAndChannel_Id(userId, channelId);
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public ReadStatus update(UUID id, Instant newLastReadAt) {
        ReadStatus updating = findById(id);
        return updating.update(newLastReadAt);
    }

    public void deleteByUserId(UUID userId) {
        ReadStatus deleting = findByUserId(userId);
        readStatusRepository.delete(deleting);
    }

    private ReadStatus findByUserId(UUID userId) {
        return readStatusRepository.findByUser_Id(userId);
    }

    public void deleteByChannelId(UUID channelId) {
        ReadStatus deleting = findByChannelId(channelId);
        readStatusRepository.delete(deleting);
    }

    private ReadStatus findByChannelId(UUID channelId) {
        return readStatusRepository.findByChannel_Id(channelId);
    }

    private void validateUserAndChannelAvailable(ReadStatus readStatus) {
        if (readStatusRepository.existsByUser_IdAndChannel_Id(readStatus.getUserId(), readStatus.getChannelId())) {
            throw new CustomException(ExceptionType.READSTATUS_ALREADY_EXISTS);
        }
    }

    private void validateUserAndChannelAvailable(List<ReadStatus> readStatuses) {
        for (ReadStatus readStatus : readStatuses) {
            validateUserAndChannelAvailable(readStatus);
        }
    }
}