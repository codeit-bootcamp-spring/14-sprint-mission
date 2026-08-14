package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_NAME = "messages.dat";

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
            log.error("메시지 데이터 파일 읽기 실패", e);

            throw new RuntimeException(e);
        }
    }

    private void saveData(Map<UUID, Message> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
            log.debug("메시지 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("메시지 데이터 파일 저장 실패", e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> data = loadData();
        data.put(message.getId(), message);

        saveData(data);
        log.debug("File 메시지 저장 완료 : id={}", message.getId());

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Message message = Optional.ofNullable(loadData().get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));
        log.debug("File 메시지 데이터 조회 : id={}", id);

        return message;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = loadData().values()
                .stream()
                .toList();
        log.debug("File 메시지 전체 조회 : count={}", messages.size());

        return messages;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = loadData();

        Message targetMessage = Optional.ofNullable(data.get(id))
                        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지가 없습니다."));

        data.remove(targetMessage.getId());

        saveData(data);
        log.debug("File 메시지 삭제 완료 : id={}", id);
    }
}
