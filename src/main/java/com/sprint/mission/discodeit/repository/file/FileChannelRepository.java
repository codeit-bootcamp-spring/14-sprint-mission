package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_NAME = "channels.dat";

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

    private void saveData(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
            log.debug("채널 데이터 파일 저장 완료 : file={}", FILE_NAME);
        } catch (IOException e) {
            log.error("채널 데이터 파일 저장 실패", e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = loadData();
        data.put(channel.getId(), channel);

        saveData(data);
        log.debug("File 채널 저장 완료 : id={}", channel.getId());

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = Optional.ofNullable(loadData().get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
        log.debug("File 채널 데이터 조회 : id={}", id);

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels =loadData().values()
                .stream()
                .toList();
        log.debug("File 채널 전체 조회 : count={}", channels.size());

        return channels;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = loadData();

        Channel targetChannel = Optional.ofNullable(data.get(id))
                        .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다."));

        data.remove(targetChannel.getId());

        saveData(data);
        log.debug("File 체널 삭제 완료 : id={}", id);
    }
}
