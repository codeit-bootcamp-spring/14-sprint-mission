package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_NAME = "messages.dat";

    private final Map<UUID, Message> cache;

    public FileMessageRepository() {
        this.cache = loadData();
    }

    private Map<UUID, Message> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            log.debug("메시지 데이터 저장 파일이 없습니다. 메시지 데이터 파일 생성 : file={}", FILE_NAME);

            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, Message> data = (Map<UUID, Message>) ois.readObject();
            log.debug("메시지 데이터 파일 읽기 완료 : size={}", data.size());

            return data;
        } catch (IOException | ClassNotFoundException e) {
            log.error("메시지 데이터 파일 읽기 실패 : " + FILE_NAME);

            throw new RuntimeException("메시지 데이터 파일 읽기 실패 : " + FILE_NAME);
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(cache);
            log.debug("메시지 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("메시지 데이터 파일 저장 실패 : " + FILE_NAME);

            throw new RuntimeException("메시지 데이터 파일 저장 실패 : " + FILE_NAME);
        }
    }

    @Override
    public Message save(Message message) {
        cache.put(message.getId(), message);

        saveData();
        log.debug("File 메시지 저장 완료 : id={}", message.getId());

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Message message = cache.get(id);
        log.debug("File 메시지 데이터 조회 : id={}", id);

        return message;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = cache.values()
                .stream()
                .toList();
        log.debug("File 메시지 전체 조회 : count={}", messages.size());

        return messages;
    }

    @Override
    public void delete(UUID id) {
        Message targetMessage = findById(id);

        cache.remove(targetMessage.getId());

        saveData();
        log.debug("File 메시지 삭제 완료 : id={}", id);
    }
}
