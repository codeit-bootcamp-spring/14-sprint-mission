package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileReadStatusRepository implements ReadStatusRepository {
    private final List<ReadStatus> readStatusList;

    public FileReadStatusRepository() {
        this.readStatusList = new ArrayList<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
        saveFile();
    }

    @Override
    public ReadStatus findById(UUID id) {
        for (ReadStatus each : readStatusList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusList;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusList.stream()
                .filter(each -> each.getChannelId() != null && each.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusList.stream()
                .filter(each -> each.getUserId() != null && each.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        ReadStatus target = findById(id);
        if (target != null) {
            readStatusList.remove(target);
            saveFile();
        }
    }

    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("readstatuslist.ser"))) {
            oos.writeObject(readStatusList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
