package com.sprint.mission.discodeit.adapter.out.persistence.readstatus;

import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReadStatusAdapter implements ReadStatusPort {
    private final ReadStatusJpaRepository repository;

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return repository.save(readStatus);
    }

    @Override
    public List<ReadStatus> saveAll(List<ReadStatus> readStatuses) {
        return repository.saveAll(readStatuses);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUserAndChannel(UUID userId, UUID channelId) {
        return repository.existsByUser_IdAndChannel_Id(userId, channelId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public List<UUID> findAllUserIdByChannelId(UUID channelId) {
        return repository.findAllUserIdByChannelId(channelId);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(ReadStatus readStatus) {
        repository.delete(readStatus);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        repository.deleteAllByUser_Id(userId);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        repository.deleteAllByChannel_Id(channelId);
    }
}
