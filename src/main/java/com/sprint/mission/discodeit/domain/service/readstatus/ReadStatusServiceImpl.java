package com.sprint.mission.discodeit.domain.service.readstatus;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ReadStatusServiceImpl implements ReadStatusService{
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus createReadStatus(ReadStatus readStatus) {

        return readStatusRepository.saveEntity(readStatus);
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {

        return readStatusRepository.findById(readStatusId)
            .orElseThrow(() ->  new CustomException(CustomErrorCode.READ_STATUS_NOT_FOUND));
    }

    @Override
    public List<ReadStatus> findAllReadStatusByChannelId(UUID channelId) {

        return readStatusRepository.findAllEntityByChannelId(channelId);
    }

    @Override
    public List<ReadStatus> findReadStatusByUserId(UUID userId) {

        return readStatusRepository.findAllReadStatusByUserId(userId);
    }

    @Override
    public void deleteReadStatusById(UUID readStatusId) {

        this.findReadStatusById(readStatusId);

        readStatusRepository.deleteEntity(readStatusId);
    }

    @Override
    public void deleteReadStatusByChannelId(UUID channelId) {

        readStatusRepository.deleteReadStatusByChannelId(channelId);
    }

    @Override
    public ReadStatus updateReadStatusReadTime(UUID readStatusId) {

        ReadStatus readStatus = this.findReadStatusById(readStatusId);
        readStatus.updateReadTime();

        return readStatusRepository.saveEntity(readStatus);
    }
}
