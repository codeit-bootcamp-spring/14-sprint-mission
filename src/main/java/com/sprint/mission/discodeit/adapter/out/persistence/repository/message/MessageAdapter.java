package com.sprint.mission.discodeit.adapter.out.persistence.repository.message;

import com.sprint.mission.discodeit.adapter.out.persistence.common.pagination.PageRequestMapper;
import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import com.sprint.mission.discodeit.core.common.pagination.PageResult;
import com.sprint.mission.discodeit.core.application.message.port.out.MessagePort;
import com.sprint.mission.discodeit.core.domain.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MessageAdapter implements MessagePort {
    private final MessageJpaRepository repository;

    @Override
    public Message save(Message message) {
        return repository.save(message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public PageResult<Message, Instant> findAllByChannelId(UUID channelId, Instant cursor, PageQuery pageQuery) {
        Slice<Message> slice = repository.findAllByChannelId(
                channelId, cursor, PageRequestMapper.from(pageQuery)
        );
        return PageResult.from(slice.getContent(), slice.getSize(), slice.hasNext(), Message::getCreatedAt);
    }

    @Override
    public Map<UUID, Instant> findLastMessageAtByChannelId(List<UUID> channelIds) {
        if (channelIds.isEmpty()) {
            return Map.of();
        }
        return repository.findLatestMessageByChannelId(channelIds).stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, UUID.class),
                        tuple -> tuple.get(1, Instant.class)
                ));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Message message) {
        repository.delete(message);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        repository.deleteAllByChannel_Id(channelId);
    }
}
