package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final List<BinaryContent> binaryContentList;

    public FileBinaryContentRepository() {
        this.binaryContentList = new ArrayList<>();
    }

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContentList.add(binaryContent);
        saveFile();
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentList.stream()
                .filter(each -> each.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        return binaryContentList.stream()
                .filter(each -> each.getUserId() != null && each.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return binaryContentList.stream()
                .filter(each -> each.getUserId() != null && each.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentList;
    }

    @Override
    public void delete(UUID id) {
        BinaryContent target = findById(id);
        if (target != null) {
            binaryContentList.remove(target);
            saveFile();
        }
    }

    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("binarycontentlist.ser"))) {
            oos.writeObject(binaryContentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
