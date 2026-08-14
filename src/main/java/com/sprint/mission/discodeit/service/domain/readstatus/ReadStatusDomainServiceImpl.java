package com.sprint.mission.discodeit.service.domain.readstatus;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Qualifier("readStatusService")
public class ReadStatusDomainServiceImpl implements ReadStatusDomainService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatusDomainServiceImpl(
            ReadStatusRepository readStatusRepository
    ) {
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public ReadStatus create(ReadStatus readStatus) {
        // 같은 유저와 채널에 대한 객체가 있으면 예외 발생
        if (readStatusRepository.existsByUserIdAndChannelId(
                readStatus.getUserId(),
                readStatus.getChannelId()
        )) {
            throw new CustomException(
                    ExceptionType.READ_STATUS_ALREADY_EXISTS,
                    readStatus.getUserId(),
                    readStatus.getChannelId()
            );
        }
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        List<ReadStatus> createdReadStatuses = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses) {
            createdReadStatuses.add(create(readStatus));
        }

        return createdReadStatuses;
    }


    @Override
    public ReadStatus findById(UUID readStatusId) {
        if (Objects.isNull(readStatusId)) {
            throw new CustomException(ExceptionType.READ_STATUS_ID_IS_NULL);
        }

        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() ->
                        new CustomException(
                                ExceptionType.READ_STATUS_NOT_FOUND,
                                readStatusId
                        )
                );
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusRepository.findAll();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        if (Objects.isNull(userId)) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }
        if (Objects.isNull(channelId)) {
            throw new CustomException(ExceptionType.CHANNEL_ID_IS_NULL);
        }

        return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new CustomException(
                        ExceptionType.READ_STATUS_NOT_FOUND_BY_USER_AND_CHANNEL,
                        userId,
                        channelId
                ));
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusRepository.existsByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public ReadStatus update(ReadStatus updatingReadStatus) {
        findById(updatingReadStatus.getId());
        return readStatusRepository.save(updatingReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        findById(readStatusId);
        readStatusRepository.delete(readStatusId);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses =
                findAllByChannelId(channelId);

        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.delete(readStatus.getId());
        }
    }
}
