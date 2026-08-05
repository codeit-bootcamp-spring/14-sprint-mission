package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel>
        implements ChannelRepository {

    public FileChannelRepository() {
        super(Files.CHANNEL);
    }

    @Override
    public void updateName(UUID id, String name) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateName(name);
            super.writeFromBufferToFile();
        });
    }

    @Override
    public void deleteUsersByUserId(UUID userId) {
        buffer.values()
                .forEach(channel -> channel.getUsersId().remove(userId));
        super.writeFromBufferToFile();
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        // 모든 유저는 PUBLIC 채널 접근 가능하물호, 조회 시 PUBLIC 채널은 반환
        return super.buffer.values().stream()
                .filter(channel -> channel.getUsersId().contains(userId) || channel.getChannelType().equals(ChannelType.PUBLIC))
                .toList();
    }
}
