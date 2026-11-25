package com.sprint.mission.discodeit.adapter.out.persistence.repository.binarycontent;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentPort;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BinaryContentAdapter implements BinaryContentPort {
    private final BinaryContentJpaRepository repository;

    @Override
    public BinaryContent save(BinaryContent content) {
        return repository.save(content);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<BinaryContent> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(BinaryContent content) {
        repository.delete(content);
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        return repository.findAllById(ids);
    }
}
