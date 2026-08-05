package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "users.dat";

    private Map<UUID, User> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            log.debug("사용자 데이터 저장 파일이 없습니다. 사용자 데이터 파일 생성 : file={}", FILE_NAME);

            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, User> data = (Map<UUID, User>) ois.readObject();
            log.debug("사용자 데이터 파일 읽기 완료 : size={}", data.size());

            return data;
        } catch (IOException | ClassNotFoundException e) {
            log.error("사용자 데이터 파일 읽기 실패", e);

            throw new RuntimeException(e);
        }
    }

    private void saveData(Map<UUID, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
            log.debug("사용자 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("사용자 데이터 파일 저장 실패", e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = loadData();
        data.put(user.getId(), user);

        saveData(data);
        log.debug("File 사용자 저장 완료 : id={}", user.getId());

        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = Optional.ofNullable(loadData().get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        log.debug("File 사용자 데이터 조회 : id={}", id);

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = loadData().values()
                .stream()
                .toList();
        log.debug("File 사용자 전체 조회 : count={}", users.size());

        return users;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = loadData();

        User targetUser = Optional.ofNullable(data.get(id))
                        .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 없습니다."));

        data.remove(targetUser.getId());
        
        saveData(data);
        log.debug("File 사용자 삭제 완료 : id={}", id);
    }
}
