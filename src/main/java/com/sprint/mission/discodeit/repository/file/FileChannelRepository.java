package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_NAME = "channels.dat";

    // 생성 시, 딱 한 번 파일 -> 메모리로 올림. 이후 이 cache가 원본
    private final Map<UUID, Channel> cache;

    // 1회만 디스크 읽는다.
    public FileChannelRepository() {
        this.cache = loadData();
    }

    private Map<UUID, Channel> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            log.debug("채널 데이터 저장 파일이 없습니다. 채널 데이터 파일 생성 : file={}", FILE_NAME);

            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, Channel> data = (Map<UUID, Channel>) ois.readObject();
            log.debug("채널 데이터 파일 읽기 완료 : size={}", data.size());

            return data;
        } catch (IOException | ClassNotFoundException e) {
            log.error("채널 데이터 읽기 실패", e);

            throw new RuntimeException(e);
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(cache);
            log.debug("채널 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("채널 데이터 파일 저장 실패 : " + FILE_NAME);

            throw new RuntimeException("채널 데이터 파일 저장 실패 : " + FILE_NAME);
        }
    }

    @Override
    public Channel save(Channel channel) {
        cache.put(channel.getId(), channel);

        saveData();
        log.debug("File 채널 저장 완료 : id={}", channel.getId());

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = cache.get(id);
        log.debug("File 채널 데이터 조회 : id={}", id);

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels = cache.values()
                .stream()
                .toList();
        log.debug("File 채널 전체 조회 : count={}", channels.size());

        return channels;
    }

    @Override
    public void delete(UUID id) {
        Channel targetChannel = findById(id);

        cache.remove(targetChannel.getId());

        saveData();
        log.debug("File 체널 삭제 완료 : id={}", id);
    }
}
