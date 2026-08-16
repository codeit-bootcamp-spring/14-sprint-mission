package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "users.dat";

    private final Map<UUID, User> cache;

    public FileUserRepository() {
        this.cache = loadData();
    }

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
            log.error("사용자 데이터 파일 읽기 실패 : " + FILE_NAME);

            throw new RuntimeException("사용자 데이터 파일 읽기 실패 : " + FILE_NAME);
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(cache);
            log.debug("사용자 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("사용자 데이터 파일 저장 실패 : " + FILE_NAME);

            throw new RuntimeException("사용자 데이터 파일 저장 실패 : " + FILE_NAME);
        }
    }

    @Override
    public User save(User user) {
        cache.put(user.getId(), user);

        saveData();
        log.debug("File 사용자 저장 완료 : id={}", user.getId());

        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = cache.get(id);
        log.debug("File 사용자 데이터 조회 : id={}", id);

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = cache.values()
                .stream()
                .toList();
        log.debug("File 사용자 전체 조회 : count={}", users.size());

        return users;
    }

    @Override
    public void delete(UUID id) {
        User targetUser = findById(id);

        cache.remove(targetUser.getId());
        
        saveData();
        log.debug("File 사용자 삭제 완료 : id={}", id);
    }
}
