package com.sprint.mission.discodeit.adapter.out.persistence.repository.channel;

import com.sprint.mission.discodeit.core.application.channel.port.out.ChannelPort;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChannelAdapter implements ChannelPort {
    private final ChannelJpaRepository repository;

    @Override
    public Channel save(Channel channel) {
        return repository.save(channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public List<Channel> findAccessibleByUserId(UUID userId) {
        return repository.findAccessibleByUserId(userId);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Channel channel) {
        repository.delete(channel);
    }
}
